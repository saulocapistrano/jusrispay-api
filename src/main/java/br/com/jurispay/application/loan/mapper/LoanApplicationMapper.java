package br.com.jurispay.application.loan.mapper;

import br.com.jurispay.application.loan.dto.LoanCreationCommand;
import br.com.jurispay.application.loan.dto.LoanResponse;
import br.com.jurispay.domain.loan.model.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para conversão entre DTOs de aplicação e modelos de domínio.
 */
@Mapper(componentModel = "spring")
public interface LoanApplicationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "dataLiberacao", ignore = true)
    @Mapping(target = "valorDevolucaoPrevista", ignore = true)
    @Mapping(target = "taxaJuros", ignore = true)
    @Mapping(target = "multaDiaria", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "quantidadeParcelas", ignore = true)
    @Mapping(target = "valorParcela", ignore = true)
    Loan toDomain(LoanCreationCommand command);

    @Mapping(target = "customerName", ignore = true)
    LoanResponse toResponse(Loan loan);
}
