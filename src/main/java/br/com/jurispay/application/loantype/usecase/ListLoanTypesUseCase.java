package br.com.jurispay.application.loantype.usecase;

import br.com.jurispay.api.dto.loantype.LoanTypeResponse;

import java.util.List;

public interface ListLoanTypesUseCase {
    List<LoanTypeResponse> listAll();
}
