package br.com.jurispay.application.customer.usecase;

import br.com.jurispay.application.customer.dto.CustomerKycStatusResponse;
import br.com.jurispay.application.customer.service.CustomerKycService;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class GetCustomerKycStatusUseCaseImpl implements GetCustomerKycStatusUseCase {

    private final CustomerKycService customerKycService;

    public GetCustomerKycStatusUseCaseImpl(CustomerKycService customerKycService) {
        this.customerKycService = customerKycService;
    }

    @Override
    public CustomerKycStatusResponse getStatus(Long customerId) {
        return customerKycService.getStatus(customerId, Instant.now());
    }
}
