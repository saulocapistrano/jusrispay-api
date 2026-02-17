package br.com.jurispay.domain.creditcheck.provider;

import br.com.jurispay.domain.creditcheck.provider.dto.CreditDataProviderResult;

public interface CreditDataProvider {

    CreditDataProviderResult consultByCpf(String cpf, String traceId);
}
