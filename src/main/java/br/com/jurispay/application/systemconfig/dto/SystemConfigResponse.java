package br.com.jurispay.application.systemconfig.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@Builder
public class SystemConfigResponse {

    private Long id;
    private String brandName;
    private String contactEmail;
    private String contactPhone;
    private String cnpj;

    private String pixKey;

    private String notificationTimezone;
    private LocalTime reminderDispatchTime;
    private LocalTime collectionDispatchTime;

    private boolean hasLogo;
    private String logoDownloadUrl;
}
