package br.com.jurispay.api.controller.loantype;

import br.com.jurispay.api.dto.loantype.LoanTypeRequest;
import br.com.jurispay.api.dto.loantype.LoanTypeResponse;
import br.com.jurispay.application.loantype.usecase.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/loan-types")
public class LoanTypeController {

    private final CreateLoanTypeUseCase createLoanTypeUseCase;
    private final UpdateLoanTypeUseCase updateLoanTypeUseCase;
    private final ActivateLoanTypeUseCase activateLoanTypeUseCase;
    private final DeactivateLoanTypeUseCase deactivateLoanTypeUseCase;
    private final ListLoanTypesUseCase listLoanTypesUseCase;
    private final ListActiveLoanTypesUseCase listActiveLoanTypesUseCase;

    public LoanTypeController(
            CreateLoanTypeUseCase createLoanTypeUseCase,
            UpdateLoanTypeUseCase updateLoanTypeUseCase,
            ActivateLoanTypeUseCase activateLoanTypeUseCase,
            DeactivateLoanTypeUseCase deactivateLoanTypeUseCase,
            ListLoanTypesUseCase listLoanTypesUseCase,
            ListActiveLoanTypesUseCase listActiveLoanTypesUseCase) {
        this.createLoanTypeUseCase = createLoanTypeUseCase;
        this.updateLoanTypeUseCase = updateLoanTypeUseCase;
        this.activateLoanTypeUseCase = activateLoanTypeUseCase;
        this.deactivateLoanTypeUseCase = deactivateLoanTypeUseCase;
        this.listLoanTypesUseCase = listLoanTypesUseCase;
        this.listActiveLoanTypesUseCase = listActiveLoanTypesUseCase;
    }

    @PostMapping
    public ResponseEntity<LoanTypeResponse> create(@Valid @RequestBody LoanTypeRequest request) {
        LoanTypeResponse response = createLoanTypeUseCase.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(location)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoanTypeResponse> update(@PathVariable Long id, @Valid @RequestBody LoanTypeRequest request) {
        LoanTypeResponse response = updateLoanTypeUseCase.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        activateLoanTypeUseCase.activate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        deactivateLoanTypeUseCase.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<LoanTypeResponse>> listAll() {
        return ResponseEntity.ok(listLoanTypesUseCase.listAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<LoanTypeResponse>> listActive() {
        return ResponseEntity.ok(listActiveLoanTypesUseCase.listActive());
    }
}
