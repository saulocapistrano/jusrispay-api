package br.com.jurispay.api.controller.collection;

import br.com.jurispay.api.dto.collection.CollectionActionRequest;
import br.com.jurispay.application.collection.dto.CollectionActionCommand;
import br.com.jurispay.application.collection.dto.CollectionActionResponse;
import br.com.jurispay.application.collection.dto.OverdueInfoResponse;
import br.com.jurispay.application.collection.usecase.GetOverdueInfoUseCase;
import br.com.jurispay.application.collection.usecase.ListCollectionActionsByLoanUseCase;
import br.com.jurispay.application.collection.usecase.RegisterCollectionActionUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controller REST para operações de Collection (cobrança).
 */
@RestController
@RequestMapping("/api/collections")
public class CollectionController {

    private final RegisterCollectionActionUseCase registerCollectionActionUseCase;
    private final ListCollectionActionsByLoanUseCase listCollectionActionsByLoanUseCase;
    private final GetOverdueInfoUseCase getOverdueInfoUseCase;

    public CollectionController(
            RegisterCollectionActionUseCase registerCollectionActionUseCase,
            ListCollectionActionsByLoanUseCase listCollectionActionsByLoanUseCase,
            GetOverdueInfoUseCase getOverdueInfoUseCase) {
        this.registerCollectionActionUseCase = registerCollectionActionUseCase;
        this.listCollectionActionsByLoanUseCase = listCollectionActionsByLoanUseCase;
        this.getOverdueInfoUseCase = getOverdueInfoUseCase;
    }

    @PostMapping
    public ResponseEntity<CollectionActionResponse> register(@Valid @RequestBody CollectionActionRequest request) {
        CollectionActionCommand command = CollectionActionCommand.builder()
                .loanId(request.getLoanId())
                .channel(request.getChannel())
                .summary(request.getSummary())
                .outcome(request.getOutcome())
                .build();

        CollectionActionResponse response = registerCollectionActionUseCase.register(command);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(location)
                .body(response);
    }

    @GetMapping("/by-loan/{loanId}")
    public ResponseEntity<List<CollectionActionResponse>> listByLoan(@PathVariable Long loanId) {
        List<CollectionActionResponse> actions = listCollectionActionsByLoanUseCase.listByLoanId(loanId);
        return ResponseEntity.ok(actions);
    }

    @GetMapping("/overdue/{loanId}")
    public ResponseEntity<OverdueInfoResponse> getOverdueInfo(@PathVariable Long loanId) {
        OverdueInfoResponse response = getOverdueInfoUseCase.getOverdueInfo(loanId);
        return ResponseEntity.ok(response);
    }
}

