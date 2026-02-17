package br.com.jurispay.application.creditcheck.usecase.impl;

import br.com.jurispay.application.creditcheck.dto.CreditCheckSummaryDto;
import br.com.jurispay.application.creditcheck.usecase.RunCreditCheckForLoanUseCase;
import br.com.jurispay.application.creditcheck.usecase.RunCreditCheckUseCase;
import br.com.jurispay.domain.customer.repository.CustomerRepository;
import br.com.jurispay.domain.exception.common.ErrorCode;
import br.com.jurispay.domain.exception.common.NotFoundException;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import org.springframework.stereotype.Service;

@Service
public class RunCreditCheckForLoanUseCaseImpl implements RunCreditCheckForLoanUseCase {

    private final LoanRepository loanRepository;
    private final CustomerRepository customerRepository;
    private final RunCreditCheckUseCase runCreditCheckUseCase;

    public RunCreditCheckForLoanUseCaseImpl(
            LoanRepository loanRepository,
            CustomerRepository customerRepository,
            RunCreditCheckUseCase runCreditCheckUseCase) {
        this.loanRepository = loanRepository;
        this.customerRepository = customerRepository;
        this.runCreditCheckUseCase = runCreditCheckUseCase;
    }

    @Override
    public CreditCheckSummaryDto run(Long loanId, Long requestedByUserId) {
        var loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.LOAN_NOT_FOUND, "Empréstimo não encontrado."));

        var customer = customerRepository.findById(loan.getCustomerId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_NOT_FOUND, "Cliente não encontrado."));

        return runCreditCheckUseCase.run(loan.getId(), customer.getId(), customer.getCpf(), requestedByUserId);
    }
}
