package br.com.jurispay.api.controller.fine;

import br.com.jurispay.api.dto.fine.FineRequest;
import br.com.jurispay.api.dto.fine.FineResponse;
import br.com.jurispay.application.fine.usecase.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/fines")
public class FineController {

    private final CreateFineUseCase createFineUseCase;
    private final UpdateFineUseCase updateFineUseCase;
    private final ActivateFineUseCase activateFineUseCase;
    private final DeactivateFineUseCase deactivateFineUseCase;
    private final ListFinesUseCase listFinesUseCase;
    private final ListActiveFinesUseCase listActiveFinesUseCase;

    public FineController(
            CreateFineUseCase createFineUseCase,
            UpdateFineUseCase updateFineUseCase,
            ActivateFineUseCase activateFineUseCase,
            DeactivateFineUseCase deactivateFineUseCase,
            ListFinesUseCase listFinesUseCase,
            ListActiveFinesUseCase listActiveFinesUseCase) {
        this.createFineUseCase = createFineUseCase;
        this.updateFineUseCase = updateFineUseCase;
        this.activateFineUseCase = activateFineUseCase;
        this.deactivateFineUseCase = deactivateFineUseCase;
        this.listFinesUseCase = listFinesUseCase;
        this.listActiveFinesUseCase = listActiveFinesUseCase;
    }

    @PostMapping
    public ResponseEntity<FineResponse> create(@Valid @RequestBody FineRequest request) {
        FineResponse response = createFineUseCase.create(request);

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
    public ResponseEntity<FineResponse> update(@PathVariable Long id, @Valid @RequestBody FineRequest request) {
        return ResponseEntity.ok(updateFineUseCase.update(id, request));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        activateFineUseCase.activate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        deactivateFineUseCase.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<FineResponse>> listAll() {
        return ResponseEntity.ok(listFinesUseCase.listAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<FineResponse>> listActive() {
        return ResponseEntity.ok(listActiveFinesUseCase.listActive());
    }
}
