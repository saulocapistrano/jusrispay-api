package br.com.jurispay.infrastructure.persistence.mapper;

import br.com.jurispay.domain.payment.model.Payment;
import br.com.jurispay.infrastructure.persistence.jpa.entity.FineEntity;
import br.com.jurispay.infrastructure.persistence.jpa.entity.LoanEntity;
import br.com.jurispay.infrastructure.persistence.jpa.entity.PaymentEntity;
import org.mapstruct.Mapper;

/**
 * Mapper para conversão entre entidades JPA e modelos de domínio de Payment.
 */
@Mapper(componentModel = "spring")
public interface PaymentEntityMapper {

    default Payment toDomain(PaymentEntity entity) {
        if (entity == null) {
            return null;
        }

        return Payment.builder()
                .id(entity.getId())
                .loanId(entity.getLoan().getId())
                .valorPago(entity.getValorPago())
                .dataPagamento(entity.getDataPagamento())
                .diasAtraso(entity.getDiasAtraso())
                .multaTotal(entity.getMultaTotal())
                .fineId(entity.getFine() != null ? entity.getFine().getId() : null)
                .fineTimes(entity.getFineTimes())
                .fineUnitAmount(entity.getFineUnitAmount())
                .fineTotalAmount(entity.getFineTotalAmount())
                .valorFinalRecebido(entity.getValorFinalRecebido())
                .roiBrl(entity.getRoiBrl())
                .roiPercent(entity.getRoiPercent())
                .metodo(entity.getMetodo())
                .dataCriacao(entity.getDataCriacao())
                .build();
    }

    default PaymentEntity toEntity(Payment payment, LoanEntity loanEntity, FineEntity fineEntity) {
        if (payment == null) {
            return null;
        }

        return PaymentEntity.builder()
                .loan(loanEntity)
                .fine(fineEntity)
                .valorPago(payment.getValorPago())
                .dataPagamento(payment.getDataPagamento())
                .diasAtraso(payment.getDiasAtraso())
                .multaTotal(payment.getMultaTotal())
                .fineTimes(payment.getFineTimes())
                .fineUnitAmount(payment.getFineUnitAmount())
                .fineTotalAmount(payment.getFineTotalAmount())
                .valorFinalRecebido(payment.getValorFinalRecebido())
                .roiBrl(payment.getRoiBrl())
                .roiPercent(payment.getRoiPercent())
                .metodo(payment.getMetodo())
                .dataCriacao(payment.getDataCriacao())
                .build();
    }
}

