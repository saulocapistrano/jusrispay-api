package br.com.jurispay.infrastructure.creditcheck;

import br.com.jurispay.domain.creditcheck.provider.CreditDataProvider;
import br.com.jurispay.domain.creditcheck.provider.dto.CreditDataProviderResult;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConditionalOnProperty(name = "app.credit-check.provider", havingValue = "FAKE", matchIfMissing = true)
public class FakeCreditDataProvider implements CreditDataProvider {

    @Override
    public CreditDataProviderResult consultByCpf(String cpf, String traceId) {
        int lastDigit = extractLastDigit(cpf);

        int score = switch (lastDigit) {
            case 0, 1 -> 320;
            case 2, 3 -> 480;
            case 4, 5 -> 610;
            case 6, 7 -> 720;
            case 8, 9 -> 830;
            default -> 550;
        };

        boolean hasRestrictions = score < 500;
        boolean hasProtests = score < 400;

        return CreditDataProviderResult.builder()
                .score(score)
                .phones(List.of("11999990000", "1133334444"))
                .emails(List.of("cliente@example.com"))
                .addresses(List.of("Rua Exemplo, 123 - Centro - São Paulo/SP"))
                .flags(Map.of(
                        "hasRestrictions", hasRestrictions,
                        "hasProtests", hasProtests,
                        "riskLevel", hasRestrictions ? "HIGH" : "LOW"
                ))
                .build();
    }

    private int extractLastDigit(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            return -1;
        }

        for (int i = cpf.length() - 1; i >= 0; i--) {
            char c = cpf.charAt(i);
            if (Character.isDigit(c)) {
                return Character.getNumericValue(c);
            }
        }

        return -1;
    }
}
