package br.com.jurispay.api.controller.contract;

import br.com.jurispay.application.contract.dto.ContractGenerationResponse;
import br.com.jurispay.application.contract.dto.ContractMessageResponse;
import br.com.jurispay.application.contract.usecase.GenerateContractMessageUseCase;
import br.com.jurispay.application.contract.usecase.GenerateContractPdfUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Controller REST para operações de Contract (contratos de empréstimo).
 */
@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    private final GenerateContractMessageUseCase generateContractMessageUseCase;
    private final GenerateContractPdfUseCase generateContractPdfUseCase;

    public ContractController(
            GenerateContractMessageUseCase generateContractMessageUseCase,
            GenerateContractPdfUseCase generateContractPdfUseCase) {
        this.generateContractMessageUseCase = generateContractMessageUseCase;
        this.generateContractPdfUseCase = generateContractPdfUseCase;
    }

    @GetMapping("/message/{loanId}")
    public ResponseEntity<ContractMessageResponse> getMessage(@PathVariable Long loanId) {
        ContractMessageResponse response = generateContractMessageUseCase.generate(loanId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/pdf/{loanId}")
    public ResponseEntity<ContractGenerationResponse> generatePdf(@PathVariable Long loanId) {
        ContractGenerationResponse response = generateContractPdfUseCase.generate(loanId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/documents/{documentId}")
                .buildAndExpand(response.getDocumentId())
                .toUri();

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(location)
                .body(response);
    }
}

