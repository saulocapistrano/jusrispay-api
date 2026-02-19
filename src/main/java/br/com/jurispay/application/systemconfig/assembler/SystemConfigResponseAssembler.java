package br.com.jurispay.application.systemconfig.assembler;

import br.com.jurispay.application.systemconfig.dto.SystemConfigResponse;
import br.com.jurispay.domain.systemconfig.model.SystemConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class SystemConfigResponseAssembler {

    public SystemConfigResponse toResponse(SystemConfig config) {
        if (config == null) {
            return null;
        }

        boolean hasLogo = config.getLogoBucket() != null
                && !config.getLogoBucket().isBlank()
                && config.getLogoObjectKey() != null
                && !config.getLogoObjectKey().isBlank();

        String logoDownloadUrl = hasLogo
                ? ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/system-config/logo")
                .toUriString()
                : null;

        return SystemConfigResponse.builder()
                .id(config.getId())
                .brandName(config.getBrandName())
                .contactEmail(config.getContactEmail())
                .contactPhone(config.getContactPhone())
                .cnpj(config.getCnpj())
                .pixKey(config.getPixKey())
                .notificationTimezone(config.getNotificationTimezone())
                .reminderDispatchTime(config.getReminderDispatchTime())
                .collectionDispatchTime(config.getCollectionDispatchTime())
                .hasLogo(hasLogo)
                .logoDownloadUrl(logoDownloadUrl)
                .build();
    }
}
