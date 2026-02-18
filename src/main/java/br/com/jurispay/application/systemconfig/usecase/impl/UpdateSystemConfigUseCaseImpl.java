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

        SystemConfig updated = SystemConfig.builder()
                .id(existing.getId())
                .brandName(normalize(command.getBrandName()))
                .contactEmail(normalize(command.getContactEmail()))
                .contactPhone(normalize(command.getContactPhone()))
                .cnpj(normalize(command.getCnpj()))
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

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String v = value.trim();
        return v.isBlank() ? null : v;
    }
}
