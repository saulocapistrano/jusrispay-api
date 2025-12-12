package br.com.jurispay.api.controller.creditanalysis;

import br.com.jurispay.api.dto.creditanalysis.CreditAnalysisDecisionRequest;
import br.com.jurispay.api.dto.creditanalysis.StartCreditAnalysisRequest;
import br.com.jurispay.application.creditanalysis.dto.CreditAnalysisDecisionCommand;
import br.com.jurispay.application.creditanalysis.dto.CreditAnalysisResponse;
import br.com.jurispay.application.creditanalysis.dto.StartCreditAnalysisCommand;
import br.com.jurispay.application.creditanalysis.usecase.DecideCreditAnalysisUseCase;
import br.com.jurispay.application.creditanalysis.usecase.GetCreditAnalysisByCustomerUseCase;
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
    private final GetCreditAnalysisByCustomerUseCase getCreditAnalysisByCustomerUseCase;

    public CreditAnalysisController(
            StartCreditAnalysisUseCase startCreditAnalysisUseCase,
            DecideCreditAnalysisUseCase decideCreditAnalysisUseCase,
            GetCreditAnalysisByCustomerUseCase getCreditAnalysisByCustomerUseCase) {
        this.startCreditAnalysisUseCase = startCreditAnalysisUseCase;
        this.decideCreditAnalysisUseCase = decideCreditAnalysisUseCase;
        this.getCreditAnalysisByCustomerUseCase = getCreditAnalysisByCustomerUseCase;
    }

    @PostMapping("/start")
    public ResponseEntity<CreditAnalysisResponse> start(@Valid @RequestBody StartCreditAnalysisRequest request) {
        StartCreditAnalysisCommand command = StartCreditAnalysisCommand.builder()
                .customerId(request.getCustomerId())
                .analystUserId(request.getAnalystUserId())
                .build();

        CreditAnalysisResponse response = startCreditAnalysisUseCase.start(command);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/../by-customer/{customerId}")
                .buildAndExpand(response.getCustomerId())
                .toUri();

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(location)
                .body(response);
    }

    @PostMapping("/decision/{customerId}")
    public ResponseEntity<CreditAnalysisResponse> decide(
            @PathVariable Long customerId,
            @Valid @RequestBody CreditAnalysisDecisionRequest request) {
        CreditAnalysisDecisionCommand command = CreditAnalysisDecisionCommand.builder()
                .customerId(customerId)
                .analystUserId(request.getAnalystUserId())
                .decisionStatus(request.getDecisionStatus())
                .rejectionReason(request.getRejectionReason())
                .notes(request.getNotes())
                .build();

        CreditAnalysisResponse response = decideCreditAnalysisUseCase.decide(command);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-customer/{customerId}")
    public ResponseEntity<CreditAnalysisResponse> getByCustomer(@PathVariable Long customerId) {
        CreditAnalysisResponse response = getCreditAnalysisByCustomerUseCase.getByCustomerId(customerId);
        return ResponseEntity.ok(response);
    }
}

