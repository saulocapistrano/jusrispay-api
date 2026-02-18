package br.com.jurispay.application.systemconfig.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SystemConfigResponse {

    private Long id;
    private String brandName;
    private String contactEmail;
    private String contactPhone;
    private String cnpj;

    private boolean hasLogo;
    private String logoDownloadUrl;
}
