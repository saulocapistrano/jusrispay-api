package br.com.jurispay.api.controller.creditcheck;

import br.com.jurispay.api.dto.creditcheck.RunCreditCheckRequest;
import br.com.jurispay.application.creditcheck.dto.CreditCheckSummaryDto;
import br.com.jurispay.application.creditcheck.usecase.GetLatestCreditCheckByLoanUseCase;
import br.com.jurispay.application.creditcheck.usecase.RunCreditCheckForLoanUseCase;
import br.com.jurispay.domain.exception.common.ErrorCode;
import br.com.jurispay.domain.exception.common.NotFoundException;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/credit-checks")
public class CreditCheckController {

    private final GetLatestCreditCheckByLoanUseCase getLatestCreditCheckByLoanUseCase;
    private final RunCreditCheckForLoanUseCase runCreditCheckForLoanUseCase;

    public CreditCheckController(
            GetLatestCreditCheckByLoanUseCase getLatestCreditCheckByLoanUseCase,
            RunCreditCheckForLoanUseCase runCreditCheckForLoanUseCase) {
        this.getLatestCreditCheckByLoanUseCase = getLatestCreditCheckByLoanUseCase;
        this.runCreditCheckForLoanUseCase = runCreditCheckForLoanUseCase;
    }

    @GetMapping("/loan/{loanId}")
    public ResponseEntity<CreditCheckSummaryDto> getLatestByLoanId(@PathVariable Long loanId) {
        CreditCheckSummaryDto response = getLatestCreditCheckByLoanUseCase.getByLoanId(loanId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND, "Credit check não encontrado para o empréstimo."));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/loan/{loanId}/run")
    public ResponseEntity<CreditCheckSummaryDto> runForLoan(
            @PathVariable Long loanId,
            @Valid @RequestBody RunCreditCheckRequest request) {
        CreditCheckSummaryDto response = runCreditCheckForLoanUseCase.run(loanId, request.getRequestedByUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
