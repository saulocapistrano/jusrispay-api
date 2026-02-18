package br.com.jurispay.domain.creditcheck.provider.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class CreditDataProviderResult {

    private Integer score;
    private List<String> phones;
    private List<String> emails;
    private List<String> addresses;
    private Map<String, Object> flags;
}
