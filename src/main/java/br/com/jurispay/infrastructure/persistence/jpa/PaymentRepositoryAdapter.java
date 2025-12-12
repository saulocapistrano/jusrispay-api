package br.com.jurispay.infrastructure.persistence.jpa;

import br.com.jurispay.domain.payment.model.Payment;
import br.com.jurispay.domain.payment.repository.PaymentRepository;
import br.com.jurispay.infrastructure.persistence.jpa.entity.LoanEntity;
import br.com.jurispay.infrastructure.persistence.jpa.entity.PaymentEntity;
import br.com.jurispay.infrastructure.persistence.jpa.repository.SpringDataLoanRepository;
import br.com.jurispay.infrastructure.persistence.jpa.repository.SpringDataPaymentRepository;
import br.com.jurispay.infrastructure.persistence.mapper.PaymentEntityMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador que implementa PaymentRepository do domínio usando JPA.
 * Converte entre entidades JPA e modelos de domínio.
 */
@Component
public class PaymentRepositoryAdapter implements PaymentRepository {

    private final SpringDataPaymentRepository springDataPaymentRepository;
    private final SpringDataLoanRepository springDataLoanRepository;
    private final PaymentEntityMapper mapper;

    public PaymentRepositoryAdapter(
            SpringDataPaymentRepository springDataPaymentRepository,
            SpringDataLoanRepository springDataLoanRepository,
            PaymentEntityMapper mapper) {
        this.springDataPaymentRepository = springDataPaymentRepository;
        this.springDataLoanRepository = springDataLoanRepository;
        this.mapper = mapper;
    }

    @Override
    public Payment save(Payment payment) {
        // Carregar LoanEntity pelo ID
        LoanEntity loanEntity = springDataLoanRepository.findById(payment.getLoanId())
                .orElseThrow(() -> new IllegalStateException("Empréstimo não encontrado para persistência do pagamento."));

        // Converter Payment -> PaymentEntity
        PaymentEntity entity = mapper.toEntity(payment, loanEntity);

        // Salvar
        PaymentEntity savedEntity = springDataPaymentRepository.save(entity);

        // Converter de volta para domínio
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return springDataPaymentRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Payment> findAll() {
        return springDataPaymentRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Payment> findByLoanId(Long loanId) {
        return springDataPaymentRepository.findByLoanId(loanId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}

