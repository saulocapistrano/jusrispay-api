package br.com.jurispay.api.controller.loan;

import br.com.jurispay.api.dto.loan.LoanRequest;
import br.com.jurispay.api.mapper.loan.LoanRequestToCommandMapper;
import br.com.jurispay.application.loan.dto.LoanResponse;
import br.com.jurispay.application.loan.usecase.CreateLoanUseCase;
import br.com.jurispay.application.loan.usecase.GetLoanByIdUseCase;
import br.com.jurispay.application.loan.usecase.ListLoansUseCase;
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
    private final LoanRequestToCommandMapper requestMapper;

    public LoanController(
            CreateLoanUseCase createLoanUseCase,
            GetLoanByIdUseCase getLoanByIdUseCase,
            ListLoansUseCase listLoansUseCase,
            LoanRequestToCommandMapper requestMapper) {
        this.createLoanUseCase = createLoanUseCase;
        this.getLoanByIdUseCase = getLoanByIdUseCase;
        this.listLoansUseCase = listLoansUseCase;
        this.requestMapper = requestMapper;
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
}

