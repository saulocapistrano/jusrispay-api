package br.com.jurispay.api.controller.creditanalysis;

import br.com.jurispay.api.dto.creditanalysis.CreditAnalysisDecisionRequest;
import br.com.jurispay.api.dto.creditanalysis.StartCreditAnalysisRequest;
import br.com.jurispay.application.creditanalysis.dto.CreditAnalysisDecisionCommand;
import br.com.jurispay.application.creditanalysis.dto.CreditAnalysisResponse;
import br.com.jurispay.application.creditanalysis.dto.StartCreditAnalysisCommand;
import br.com.jurispay.application.creditanalysis.usecase.DecideCreditAnalysisUseCase;
import br.com.jurispay.application.creditanalysis.usecase.GetCreditAnalysisByLoanUseCase;
import br.com.jurispay.application.creditanalysis.usecase.StartCreditAnalysisUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Controller REST para operações de CreditAnalysis.
 */
@RestController
@RequestMapping("/api/credit-analyses")
public class CreditAnalysisController {

    private final StartCreditAnalysisUseCase startCreditAnalysisUseCase;
    private final DecideCreditAnalysisUseCase decideCreditAnalysisUseCase;
    private final GetCreditAnalysisByLoanUseCase getCreditAnalysisByLoanUseCase;

    public CreditAnalysisController(
            StartCreditAnalysisUseCase startCreditAnalysisUseCase,
            DecideCreditAnalysisUseCase decideCreditAnalysisUseCase,
            GetCreditAnalysisByLoanUseCase getCreditAnalysisByLoanUseCase) {
        this.startCreditAnalysisUseCase = startCreditAnalysisUseCase;
        this.decideCreditAnalysisUseCase = decideCreditAnalysisUseCase;
        this.getCreditAnalysisByLoanUseCase = getCreditAnalysisByLoanUseCase;
    }

    @PostMapping("/start")
    public ResponseEntity<CreditAnalysisResponse> start(@Valid @RequestBody StartCreditAnalysisRequest request) {
        StartCreditAnalysisCommand command = StartCreditAnalysisCommand.builder()
                .loanId(request.getLoanId())
                .analystUserId(request.getAnalystUserId())
                .build();

        CreditAnalysisResponse response = startCreditAnalysisUseCase.start(command);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/../by-loan/{loanId}")
                .buildAndExpand(response.getLoanId())
                .toUri();

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(location)
                .body(response);
    }

    @PostMapping("/decision/{loanId}")
    public ResponseEntity<CreditAnalysisResponse> decide(
            @PathVariable Long loanId,
            @Valid @RequestBody CreditAnalysisDecisionRequest request) {
        CreditAnalysisDecisionCommand command = CreditAnalysisDecisionCommand.builder()
                .loanId(loanId)
                .analystUserId(request.getAnalystUserId())
                .decisionStatus(request.getDecisionStatus())
                .rejectionReason(request.getRejectionReason())
                .notes(request.getNotes())
                .build();

        CreditAnalysisResponse response = decideCreditAnalysisUseCase.decide(command);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-loan/{loanId}")
    public ResponseEntity<CreditAnalysisResponse> getByLoan(@PathVariable Long loanId) {
        CreditAnalysisResponse response = getCreditAnalysisByLoanUseCase.getByLoanId(loanId);
        return ResponseEntity.ok(response);
    }
}

