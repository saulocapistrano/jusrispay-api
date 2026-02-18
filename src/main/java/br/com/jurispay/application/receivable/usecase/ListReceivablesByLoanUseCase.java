package br.com.jurispay.application.receivable.usecase;

import br.com.jurispay.application.receivable.dto.ReceivableResponse;

import java.util.List;

public interface ListReceivablesByLoanUseCase {

    List<ReceivableResponse> listByLoanId(Long loanId);
}
