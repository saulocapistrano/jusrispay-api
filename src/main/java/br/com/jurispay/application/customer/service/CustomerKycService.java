package br.com.jurispay.application.customer.service;

import br.com.jurispay.application.customer.dto.CustomerKycStatusResponse;
import br.com.jurispay.domain.collection.repository.CollectionRepository;
import br.com.jurispay.domain.creditanalysis.specification.DocumentChecklistSpecification;
import br.com.jurispay.domain.customer.repository.CustomerRepository;
import br.com.jurispay.domain.document.model.Document;
import br.com.jurispay.domain.document.model.DocumentStatus;
import br.com.jurispay.domain.document.model.DocumentType;
import br.com.jurispay.domain.document.repository.DocumentRepository;
import br.com.jurispay.domain.exception.common.ErrorCode;
import br.com.jurispay.domain.exception.common.NotFoundException;
import br.com.jurispay.domain.exception.common.ValidationException;
import br.com.jurispay.domain.loan.model.Loan;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import br.com.jurispay.domain.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomerKycService {

    private static final long KYC_VALIDITY_DAYS = 180;
    private static final long INACTIVITY_DAYS = 30;

    private final CustomerRepository customerRepository;
    private final DocumentRepository documentRepository;
    private final LoanRepository loanRepository;
    private final PaymentRepository paymentRepository;
    private final CollectionRepository collectionRepository;
    private final DocumentChecklistSpecification documentChecklistSpecification;

    public CustomerKycService(
            CustomerRepository customerRepository,
            DocumentRepository documentRepository,
            LoanRepository loanRepository,
            PaymentRepository paymentRepository,
            CollectionRepository collectionRepository,
            DocumentChecklistSpecification documentChecklistSpecification) {
        this.customerRepository = customerRepository;
        this.documentRepository = documentRepository;
        this.loanRepository = loanRepository;
        this.paymentRepository = paymentRepository;
        this.collectionRepository = collectionRepository;
        this.documentChecklistSpecification = documentChecklistSpecification;
    }

    public CustomerKycStatusResponse getStatus(Long customerId, Instant now) {
        customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_NOT_FOUND, "Cliente não encontrado"));

        List<Document> documents = documentRepository.findByCustomerId(customerId);

        Set<DocumentType> validatedDocumentTypes = documents.stream()
                .filter(doc -> doc.getStatus() == DocumentStatus.VALIDATED)
                .map(Document::getType)
                .collect(Collectors.toSet());

        boolean checklistSatisfied = documentChecklistSpecification.isSatisfiedBy(validatedDocumentTypes);
        List<DocumentType> missing = documentChecklistSpecification.getRequiredDocumentTypes().stream()
                .filter(required -> !validatedDocumentTypes.contains(required))
                .sorted(Comparator.comparing(Enum::name))
                .collect(Collectors.toList());

        Optional<Instant> lastValidatedAt = documents.stream()
                .filter(doc -> doc.getStatus() == DocumentStatus.VALIDATED)
                .map(Document::getValidatedAt)
                .filter(v -> v != null)
                .max(Instant::compareTo);

        Optional<Instant> lastMovementAt = getLastMovementAt(customerId);

        boolean inactive = lastMovementAt.isEmpty() || ChronoUnit.DAYS.between(lastMovementAt.get(), now) > INACTIVITY_DAYS;
        boolean expired = lastValidatedAt.isPresent() && ChronoUnit.DAYS.between(lastValidatedAt.get(), now) > KYC_VALIDITY_DAYS;

        boolean needsRevalidation = checklistSatisfied && expired && inactive;
        boolean complete = checklistSatisfied && !needsRevalidation;

        return CustomerKycStatusResponse.builder()
                .customerId(customerId)
                .complete(complete)
                .needsRevalidation(needsRevalidation)
                .lastValidatedAt(lastValidatedAt.orElse(null))
                .lastMovementAt(lastMovementAt.orElse(null))
                .missingDocumentTypes(missing)
                .build();
    }

    public void validateCustomerEligibleForLoan(Long customerId, Instant now) {
        CustomerKycStatusResponse status = getStatus(customerId, now);
        if (status.isNeedsRevalidation()) {
            throw new ValidationException(
                    ErrorCode.KYC_REVALIDATION_REQUIRED,
                    "Revalidação de KYC obrigatória: cliente inativo por mais de 30 dias e documentos com mais de 6 meses."
            );
        }
        if (!status.isComplete()) {
            throw new ValidationException(
                    ErrorCode.KYC_INCOMPLETE,
                    "KYC incompleto: documentos obrigatórios ausentes ou não validados."
            );
        }
    }

    private Optional<Instant> getLastMovementAt(Long customerId) {
        List<Loan> loans = loanRepository.findByCustomerId(customerId);
        List<Long> loanIds = loans.stream().map(Loan::getId).filter(id -> id != null).collect(Collectors.toList());
        if (loanIds.isEmpty()) {
            return Optional.empty();
        }

        Optional<Instant> lastPaymentAt = paymentRepository.findLastPaymentAtByLoanIds(loanIds);
        Optional<Instant> lastActionAt = collectionRepository.findLastActionAtByLoanIds(loanIds);

        if (lastPaymentAt.isEmpty()) {
            return lastActionAt;
        }
        if (lastActionAt.isEmpty()) {
            return lastPaymentAt;
        }
        return Optional.of(lastPaymentAt.get().isAfter(lastActionAt.get()) ? lastPaymentAt.get() : lastActionAt.get());
    }
}
