package br.com.jurispay.api.controller.customer;

import br.com.jurispay.api.dto.customer.CustomerRequest;
import br.com.jurispay.application.customer.dto.CustomerRegistrationCommand;
import br.com.jurispay.application.customer.dto.CustomerResponse;
import br.com.jurispay.application.customer.usecase.GetCustomerByIdUseCase;
import br.com.jurispay.application.customer.usecase.ListCustomersUseCase;
import br.com.jurispay.application.customer.usecase.RegisterCustomerUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controller REST para operações de Customer.
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final RegisterCustomerUseCase registerUseCase;
    private final GetCustomerByIdUseCase getByIdUseCase;
    private final ListCustomersUseCase listCustomersUseCase;

    public CustomerController(
            RegisterCustomerUseCase registerUseCase,
            GetCustomerByIdUseCase getByIdUseCase,
            ListCustomersUseCase listCustomersUseCase) {
        this.registerUseCase = registerUseCase;
        this.getByIdUseCase = getByIdUseCase;
        this.listCustomersUseCase = listCustomersUseCase;
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CustomerRequest request) {
        CustomerRegistrationCommand command = CustomerRegistrationCommand.builder()
                .nomeCompleto(request.getNomeCompleto())
                .cpf(request.getCpf())
                .telefone(request.getTelefone())
                .chavePix(request.getChavePix())
                .rendaMensal(request.getRendaMensal())
                .ocupacaoAtual(request.getOcupacaoAtual())
                .redesSociais(request.getRedesSociais())
                .build();

        CustomerResponse response = registerUseCase.register(command);

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
    public ResponseEntity<CustomerResponse> getById(@PathVariable Long id) {
        CustomerResponse response = getByIdUseCase.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> listAll() {
        List<CustomerResponse> customers = listCustomersUseCase.listAll();
        return ResponseEntity.ok(customers);
    }
}

