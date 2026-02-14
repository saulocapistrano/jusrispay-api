package br.com.jurispay.infrastructure.persistence.mapper;

import br.com.jurispay.domain.receivable.model.Receivable;
import br.com.jurispay.infrastructure.persistence.jpa.entity.LoanEntity;
import br.com.jurispay.infrastructure.persistence.jpa.entity.ReceivableEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReceivableEntityMapper {

    default Receivable toDomain(ReceivableEntity entity) {
        if (entity == null) {
            return null;
        }
        return Receivable.builder()
                .id(entity.getId())
                .loanId(entity.getLoan().getId())
                .installmentNumber(entity.getInstallmentNumber())
                .dueDate(entity.getDueDate())
                .amount(entity.getAmount())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .paidAt(entity.getPaidAt())
                .build();
    }

    default ReceivableEntity toEntity(Receivable receivable, LoanEntity loanEntity) {
        if (receivable == null) {
            return null;
        }
        return ReceivableEntity.builder()
                .id(receivable.getId())
                .loan(loanEntity)
                .installmentNumber(receivable.getInstallmentNumber())
                .dueDate(receivable.getDueDate())
                .amount(receivable.getAmount())
                .status(receivable.getStatus())
                .createdAt(receivable.getCreatedAt())
                .paidAt(receivable.getPaidAt())
                .build();
    }
}
