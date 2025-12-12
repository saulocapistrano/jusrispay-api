package br.com.jurispay.application.payment.usecase;

import br.com.jurispay.application.payment.dto.PaymentResponse;
import br.com.jurispay.application.payment.mapper.PaymentApplicationMapper;
import br.com.jurispay.domain.common.exception.ValidationException;
import br.com.jurispay.domain.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação do caso de uso de listagem de pagamentos.
 */
@Service
public class ListPaymentsUseCaseImpl implements ListPaymentsUseCase {

    private final PaymentRepository paymentRepository;
    private final PaymentApplicationMapper mapper;

    public ListPaymentsUseCaseImpl(
            PaymentRepository paymentRepository,
            PaymentApplicationMapper mapper) {
        this.paymentRepository = paymentRepository;
        this.mapper = mapper;
    }

    @Override
    public List<PaymentResponse> listAll() {
        return paymentRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentResponse> listByLoanId(Long loanId) {
        if (loanId == null) {
            throw new ValidationException("loanId é obrigatório.");
        }

        return paymentRepository.findByLoanId(loanId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}

