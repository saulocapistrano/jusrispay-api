package br.com.jurispay.api.controller.loan;

import br.com.jurispay.api.dto.loan.LoanRequest;
import br.com.jurispay.api.mapper.loan.LoanRequestToCommandMapper;
import br.com.jurispay.application.risk.dto.LoanRiskAssessmentResponse;
import br.com.jurispay.application.risk.usecase.GetLoanRiskAssessmentUseCase;
import br.com.jurispay.application.loan.dto.LoanResponse;
import br.com.jurispay.application.loan.usecase.CreateLoanUseCase;
import br.com.jurispay.application.loan.usecase.CreditLoanUseCase;
import br.com.jurispay.application.loan.usecase.GetLoanByIdUseCase;
import br.com.jurispay.application.loan.usecase.ListLoansUseCase;
import br.com.jurispay.application.loan.usecase.SyncLoanStatusFromCreditAnalysisUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controller REST para operações de Loan (Empréstimo).
 */
@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final CreateLoanUseCase createLoanUseCase;
    private final GetLoanByIdUseCase getLoanByIdUseCase;
    private final ListLoansUseCase listLoansUseCase;
    private final CreditLoanUseCase creditLoanUseCase;
    private final SyncLoanStatusFromCreditAnalysisUseCase syncLoanStatusFromCreditAnalysisUseCase;
    private final LoanRequestToCommandMapper requestMapper;
    private final GetLoanRiskAssessmentUseCase getLoanRiskAssessmentUseCase;

    public LoanController(
            CreateLoanUseCase createLoanUseCase,
            GetLoanByIdUseCase getLoanByIdUseCase,
            ListLoansUseCase listLoansUseCase,
            CreditLoanUseCase creditLoanUseCase,
            SyncLoanStatusFromCreditAnalysisUseCase syncLoanStatusFromCreditAnalysisUseCase,
            LoanRequestToCommandMapper requestMapper,
            GetLoanRiskAssessmentUseCase getLoanRiskAssessmentUseCase) {
        this.createLoanUseCase = createLoanUseCase;
        this.getLoanByIdUseCase = getLoanByIdUseCase;
        this.listLoansUseCase = listLoansUseCase;
        this.creditLoanUseCase = creditLoanUseCase;
        this.syncLoanStatusFromCreditAnalysisUseCase = syncLoanStatusFromCreditAnalysisUseCase;
        this.requestMapper = requestMapper;
        this.getLoanRiskAssessmentUseCase = getLoanRiskAssessmentUseCase;
    }

    @PostMapping
    public ResponseEntity<LoanResponse> create(@Valid @RequestBody LoanRequest request) {
        LoanResponse response = createLoanUseCase.create(requestMapper.toCommand(request));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(location)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanResponse> getById(@PathVariable Long id) {
        LoanResponse response = getLoanByIdUseCase.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<LoanResponse>> listAll() {
        List<LoanResponse> loans = listLoansUseCase.listAll();
        return ResponseEntity.ok(loans);
    }

    @PostMapping("/{id}/credit")
    public ResponseEntity<LoanResponse> credit(@PathVariable Long id) {
        LoanResponse response = creditLoanUseCase.credit(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/sync-status-from-analysis")
    public ResponseEntity<LoanResponse> syncStatusFromAnalysis(@PathVariable Long id) {
        LoanResponse response = syncLoanStatusFromCreditAnalysisUseCase.sync(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/risk-assessment")
    public ResponseEntity<LoanRiskAssessmentResponse> getRiskAssessment(@PathVariable Long id) {
        LoanRiskAssessmentResponse response = getLoanRiskAssessmentUseCase.getByLoanId(id);
        return ResponseEntity.ok(response);
    }
}
