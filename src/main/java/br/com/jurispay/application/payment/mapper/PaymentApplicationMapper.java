package br.com.jurispay.application.payment.mapper;

import br.com.jurispay.application.payment.dto.PaymentRegistrationCommand;
import br.com.jurispay.application.payment.dto.PaymentResponse;
import br.com.jurispay.domain.payment.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para conversão entre DTOs de aplicação e modelos de domínio de Payment.
 */
@Mapper(componentModel = "spring")
public interface PaymentApplicationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "diasAtraso", ignore = true)
    @Mapping(target = "multaTotal", ignore = true)
    @Mapping(target = "fineId", ignore = true)
    @Mapping(target = "fineTimes", ignore = true)
    @Mapping(target = "fineUnitAmount", ignore = true)
    @Mapping(target = "fineTotalAmount", ignore = true)
    @Mapping(target = "valorFinalRecebido", ignore = true)
    @Mapping(target = "roiBrl", ignore = true)
    @Mapping(target = "roiPercent", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    Payment toDomain(PaymentRegistrationCommand command);

    PaymentResponse toResponse(Payment payment);
}

