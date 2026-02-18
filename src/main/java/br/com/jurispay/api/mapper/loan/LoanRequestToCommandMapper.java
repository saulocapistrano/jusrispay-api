package br.com.jurispay.api.mapper.loan;

import br.com.jurispay.api.dto.loan.LoanRequest;
import br.com.jurispay.application.loan.dto.LoanCreationCommand;
import org.mapstruct.Mapper;

/**
 * Mapper MapStruct para conversão de LoanRequest (API) para LoanCreationCommand (Application).
 */
@Mapper(componentModel = "spring")
public interface LoanRequestToCommandMapper {

    /**
     * Converte LoanRequest para LoanCreationCommand.
     *
     * @param request requisição da API
     * @return comando de criação de empréstimo
     */
    LoanCreationCommand toCommand(LoanRequest request);
}

