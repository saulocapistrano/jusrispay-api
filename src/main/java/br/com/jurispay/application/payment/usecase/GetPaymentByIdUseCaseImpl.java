package br.com.jurispay.application.payment.usecase;

import br.com.jurispay.application.payment.dto.PaymentResponse;
import br.com.jurispay.application.payment.mapper.PaymentApplicationMapper;
import br.com.jurispay.domain.common.exception.NotFoundException;
import br.com.jurispay.domain.payment.model.Payment;
import br.com.jurispay.domain.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

/**
 * Implementação do caso de uso de busca de pagamento por ID.
 */
@Service
public class GetPaymentByIdUseCaseImpl implements GetPaymentByIdUseCase {

    private final PaymentRepository paymentRepository;
    private final PaymentApplicationMapper mapper;

    public GetPaymentByIdUseCaseImpl(
            PaymentRepository paymentRepository,
            PaymentApplicationMapper mapper) {
        this.paymentRepository = paymentRepository;
        this.mapper = mapper;
    }

    @Override
    public PaymentResponse getById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pagamento não encontrado."));

        return mapper.toResponse(payment);
    }
}

