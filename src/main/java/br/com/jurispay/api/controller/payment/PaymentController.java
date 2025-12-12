package br.com.jurispay.api.controller.payment;

import br.com.jurispay.api.dto.payment.PaymentRequest;
import br.com.jurispay.application.payment.dto.PaymentRegistrationCommand;
import br.com.jurispay.application.payment.dto.PaymentResponse;
import br.com.jurispay.application.payment.usecase.GetPaymentByIdUseCase;
import br.com.jurispay.application.payment.usecase.ListPaymentsUseCase;
import br.com.jurispay.application.payment.usecase.RegisterPaymentUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controller REST para operações de Payment (Pagamento).
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final RegisterPaymentUseCase registerPaymentUseCase;
    private final GetPaymentByIdUseCase getPaymentByIdUseCase;
    private final ListPaymentsUseCase listPaymentsUseCase;

    public PaymentController(
            RegisterPaymentUseCase registerPaymentUseCase,
            GetPaymentByIdUseCase getPaymentByIdUseCase,
            ListPaymentsUseCase listPaymentsUseCase) {
        this.registerPaymentUseCase = registerPaymentUseCase;
        this.getPaymentByIdUseCase = getPaymentByIdUseCase;
        this.listPaymentsUseCase = listPaymentsUseCase;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> create(@Valid @RequestBody PaymentRequest request) {
        PaymentRegistrationCommand command = PaymentRegistrationCommand.builder()
                .loanId(request.getLoanId())
                .valorPago(request.getValorPago())
                .dataPagamento(request.getDataPagamento())
                .metodo(request.getMetodo())
                .build();

        PaymentResponse response = registerPaymentUseCase.register(command);

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
    public ResponseEntity<PaymentResponse> getById(@PathVariable Long id) {
        PaymentResponse response = getPaymentByIdUseCase.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> listAll() {
        List<PaymentResponse> payments = listPaymentsUseCase.listAll();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/by-loan/{loanId}")
    public ResponseEntity<List<PaymentResponse>> listByLoanId(@PathVariable Long loanId) {
        List<PaymentResponse> payments = listPaymentsUseCase.listByLoanId(loanId);
        return ResponseEntity.ok(payments);
    }
}

