package br.com.jurispay.application.customer.usecase;

import br.com.jurispay.application.customer.dto.CustomerKycStatusResponse;

public interface GetCustomerKycStatusUseCase {

    CustomerKycStatusResponse getStatus(Long customerId);
}
