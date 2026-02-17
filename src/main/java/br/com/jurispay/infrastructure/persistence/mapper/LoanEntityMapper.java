package br.com.jurispay.infrastructure.persistence.mapper;

import br.com.jurispay.domain.loan.model.Loan;
import br.com.jurispay.infrastructure.persistence.jpa.entity.CustomerEntity;
import br.com.jurispay.infrastructure.persistence.jpa.entity.LoanEntity;
import br.com.jurispay.infrastructure.persistence.jpa.entity.LoanTypeEntity;
import org.mapstruct.Mapper;

/**
 * Mapper para conversão entre entidades JPA e modelos de domínio de Loan.
 */
@Mapper(componentModel = "spring")
public interface LoanEntityMapper {

    default Loan toDomain(LoanEntity entity) {
        if (entity == null) {
            return null;
        }

        return Loan.builder()
                .id(entity.getId())
                .customerId(entity.getCustomer().getId())
                .loanTypeId(entity.getLoanType() != null ? entity.getLoanType().getId() : null)
                .valorSolicitado(entity.getValorSolicitado())
                .valorDevolucaoPrevista(entity.getValorDevolucaoPrevista())
                .taxaJuros(entity.getTaxaJuros())
                .multaDiaria(entity.getMultaDiaria())
                .periodoPagamento(entity.getPeriodoPagamento())
                .quantidadeParcelas(entity.getQuantidadeParcelas())
                .valorParcela(entity.getValorParcela())
                .dataLiberacao(entity.getDataLiberacao())
                .dataPrevistaDevolucao(entity.getDataPrevistaDevolucao())
                .dataCriacao(entity.getDataCriacao())
                .dataAtualizacao(entity.getDataAtualizacao())
                .status(entity.getStatus())
                .build();
    }

    default LoanEntity toEntity(Loan loan, CustomerEntity customerEntity) {
        if (loan == null) {
            return null;
        }

        return LoanEntity.builder()
                .id(loan.getId())
                .customer(customerEntity)
                .loanType(loan.getLoanTypeId() != null ? LoanTypeEntity.builder().id(loan.getLoanTypeId()).build() : null)
                .valorSolicitado(loan.getValorSolicitado())
                .valorDevolucaoPrevista(loan.getValorDevolucaoPrevista())
                .taxaJuros(loan.getTaxaJuros())
                .multaDiaria(loan.getMultaDiaria())
                .periodoPagamento(loan.getPeriodoPagamento())
                .quantidadeParcelas(loan.getQuantidadeParcelas())
                .valorParcela(loan.getValorParcela())
                .dataLiberacao(loan.getDataLiberacao())
                .dataPrevistaDevolucao(loan.getDataPrevistaDevolucao())
                .dataCriacao(loan.getDataCriacao())
                .dataAtualizacao(loan.getDataAtualizacao())
                .status(loan.getStatus())
                .build();
    }
}

