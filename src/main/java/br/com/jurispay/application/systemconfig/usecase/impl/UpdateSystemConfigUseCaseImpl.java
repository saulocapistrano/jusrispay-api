package br.com.jurispay.application.systemconfig.usecase.impl;

import br.com.jurispay.application.systemconfig.assembler.SystemConfigResponseAssembler;
import br.com.jurispay.application.systemconfig.dto.SystemConfigResponse;
import br.com.jurispay.application.systemconfig.dto.UpdateSystemConfigCommand;
import br.com.jurispay.application.systemconfig.service.SystemConfigDefaults;
import br.com.jurispay.application.systemconfig.usecase.UpdateSystemConfigUseCase;
import br.com.jurispay.domain.systemconfig.model.SystemConfig;
import br.com.jurispay.domain.systemconfig.repository.SystemConfigRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalTime;

@Service
public class UpdateSystemConfigUseCaseImpl implements UpdateSystemConfigUseCase {

    private static final Long DEFAULT_ID = 1L;

    private final SystemConfigRepository repository;
    private final SystemConfigResponseAssembler assembler;

    public UpdateSystemConfigUseCaseImpl(SystemConfigRepository repository, SystemConfigResponseAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @Override
    public SystemConfigResponse update(UpdateSystemConfigCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Command não pode ser nulo.");
        }

        var existing = repository.findById(DEFAULT_ID).orElseGet(() -> repository.save(SystemConfigDefaults.defaultConfig(DEFAULT_ID)));
        Instant now = Instant.now();

        String timezone = resolveUpdatedTimezone(existing.getNotificationTimezone(), command.getNotificationTimezone());
        LocalTime reminderTime = resolveUpdatedLocalTime(existing.getReminderDispatchTime(), command.getReminderDispatchTime());
        LocalTime collectionTime = resolveUpdatedLocalTime(existing.getCollectionDispatchTime(), command.getCollectionDispatchTime());

        if (timezone == null || timezone.isBlank()) {
            timezone = "America/Fortaleza";
        }
        if (reminderTime == null) {
            reminderTime = LocalTime.of(9, 0);
        }
        if (collectionTime == null) {
            collectionTime = LocalTime.of(10, 0);
        }

        SystemConfig updated = SystemConfig.builder()
                .id(existing.getId())
                .brandName(resolveUpdatedString(existing.getBrandName(), command.getBrandName()))
                .contactEmail(resolveUpdatedString(existing.getContactEmail(), command.getContactEmail()))
                .contactPhone(resolveUpdatedString(existing.getContactPhone(), command.getContactPhone()))
                .cnpj(resolveUpdatedString(existing.getCnpj(), command.getCnpj()))

                .pixKey(resolveUpdatedString(existing.getPixKey(), command.getPixKey()))
                .notificationTimezone(timezone)
                .reminderDispatchTime(reminderTime)
                .collectionDispatchTime(collectionTime)
                .logoOriginalFileName(existing.getLogoOriginalFileName())
                .logoContentType(existing.getLogoContentType())
                .logoSizeBytes(existing.getLogoSizeBytes())
                .logoBucket(existing.getLogoBucket())
                .logoObjectKey(existing.getLogoObjectKey())
                .createdAt(existing.getCreatedAt())
                .updatedAt(now)
                .build();

        var saved = repository.save(updated);
        return assembler.toResponse(saved);
    }

    private String resolveUpdatedString(String existingValue, String incomingValue) {
        if (incomingValue == null) {
            return existingValue;
        }
        String v = incomingValue.trim();
        return v.isBlank() ? null : v;
    }

    private String resolveUpdatedTimezone(String existingValue, String incomingValue) {
        if (incomingValue == null) {
            return existingValue;
        }
        String v = incomingValue.trim();
        if (v.isBlank()) {
            return null;
        }
        return v;
    }

    private LocalTime resolveUpdatedLocalTime(LocalTime existingValue, LocalTime incomingValue) {
        if (incomingValue == null) {
            return existingValue;
        }
        return incomingValue;
    }
}
