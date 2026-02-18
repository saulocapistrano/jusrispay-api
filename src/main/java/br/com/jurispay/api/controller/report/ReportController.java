package br.com.jurispay.api.controller.report;

import br.com.jurispay.application.report.dto.DueLoanItemResponse;
import br.com.jurispay.application.report.dto.OverdueLoanItemResponse;
import br.com.jurispay.application.report.dto.PortfolioSummaryResponse;
import br.com.jurispay.application.report.usecase.GetPortfolioSummaryUseCase;
import br.com.jurispay.application.report.usecase.ListNextDueLoansUseCase;
import br.com.jurispay.application.report.usecase.ListOverdueLoansUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para operações de relatórios e dashboard.
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final GetPortfolioSummaryUseCase getPortfolioSummaryUseCase;
    private final ListNextDueLoansUseCase listNextDueLoansUseCase;
    private final ListOverdueLoansUseCase listOverdueLoansUseCase;

    public ReportController(
            GetPortfolioSummaryUseCase getPortfolioSummaryUseCase,
            ListNextDueLoansUseCase listNextDueLoansUseCase,
            ListOverdueLoansUseCase listOverdueLoansUseCase) {
        this.getPortfolioSummaryUseCase = getPortfolioSummaryUseCase;
        this.listNextDueLoansUseCase = listNextDueLoansUseCase;
        this.listOverdueLoansUseCase = listOverdueLoansUseCase;
    }

    @GetMapping("/portfolio")
    public ResponseEntity<PortfolioSummaryResponse> getPortfolioSummary() {
        PortfolioSummaryResponse response = getPortfolioSummaryUseCase.get();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/due")
    public ResponseEntity<List<DueLoanItemResponse>> listNextDueLoans(
            @RequestParam(defaultValue = "20") int limit) {
        List<DueLoanItemResponse> response = listNextDueLoansUseCase.list(limit);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<OverdueLoanItemResponse>> listOverdueLoans(
            @RequestParam(defaultValue = "20") int limit) {
        List<OverdueLoanItemResponse> response = listOverdueLoansUseCase.list(limit);
        return ResponseEntity.ok(response);
    }
}

