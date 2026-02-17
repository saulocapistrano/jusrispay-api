package br.com.jurispay.api.controller.receivable;

import br.com.jurispay.api.dto.receivable.PayReceivableRequest;
import br.com.jurispay.application.receivable.dto.PayReceivableCommand;
import br.com.jurispay.application.receivable.dto.ReceivableResponse;
import br.com.jurispay.application.receivable.usecase.ListReceivablesByLoanUseCase;
import br.com.jurispay.application.receivable.usecase.PayReceivableUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receivables")
public class ReceivableController {

    private final ListReceivablesByLoanUseCase listReceivablesByLoanUseCase;
    private final PayReceivableUseCase payReceivableUseCase;

    public ReceivableController(
            ListReceivablesByLoanUseCase listReceivablesByLoanUseCase,
            PayReceivableUseCase payReceivableUseCase) {
        this.listReceivablesByLoanUseCase = listReceivablesByLoanUseCase;
        this.payReceivableUseCase = payReceivableUseCase;
    }

    @GetMapping("/by-loan/{loanId}")
    public ResponseEntity<List<ReceivableResponse>> listByLoanId(@PathVariable Long loanId) {
        return ResponseEntity.ok(listReceivablesByLoanUseCase.listByLoanId(loanId));
    }

    @PostMapping("/{receivableId}/pay")
    public ResponseEntity<ReceivableResponse> pay(
            @PathVariable Long receivableId,
            @Valid @RequestBody PayReceivableRequest request) {
        ReceivableResponse response = payReceivableUseCase.pay(PayReceivableCommand.builder()
                .receivableId(receivableId)
                .adimplente(request.getAdimplente())
                .build());
        return ResponseEntity.ok(response);
    }
}
