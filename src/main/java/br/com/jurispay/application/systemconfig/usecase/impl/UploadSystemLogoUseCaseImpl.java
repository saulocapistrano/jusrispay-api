package br.com.jurispay.application.systemconfig.usecase.impl;

import br.com.jurispay.application.systemconfig.assembler.SystemConfigResponseAssembler;
import br.com.jurispay.application.systemconfig.dto.SystemConfigResponse;
import br.com.jurispay.application.systemconfig.service.SystemConfigDefaults;
import br.com.jurispay.application.systemconfig.usecase.UploadSystemLogoUseCase;
import br.com.jurispay.domain.document.model.PutFileCommand;
import br.com.jurispay.domain.document.repository.FileStorageRepository;
import br.com.jurispay.domain.exception.common.ValidationException;
import br.com.jurispay.domain.systemconfig.model.SystemConfig;
import br.com.jurispay.domain.systemconfig.repository.SystemConfigRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Service
public class UploadSystemLogoUseCaseImpl implements UploadSystemLogoUseCase {

    private static final Long DEFAULT_ID = 1L;
    private static final String DEFAULT_BUCKET = "jurispay-system";
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("image/png", "image/jpeg", "image/webp");

    private final SystemConfigRepository repository;
    private final FileStorageRepository fileStorageRepository;
    private final SystemConfigResponseAssembler assembler;

    public UploadSystemLogoUseCaseImpl(
            SystemConfigRepository repository,
            FileStorageRepository fileStorageRepository,
            SystemConfigResponseAssembler assembler) {
        this.repository = repository;
        this.fileStorageRepository = fileStorageRepository;
        this.assembler = assembler;
    }

    @Override
    public SystemConfigResponse upload(byte[] bytes, String originalFileName, String contentType) {
        validate(bytes, originalFileName, contentType);

        var existing = repository.findById(DEFAULT_ID)
                .orElseGet(() -> repository.save(SystemConfigDefaults.defaultConfig(DEFAULT_ID)));

        String objectKey = generateObjectKey(originalFileName);

        fileStorageRepository.put(PutFileCommand.builder()
                .bucket(DEFAULT_BUCKET)
                .objectKey(objectKey)
                .originalFileName(originalFileName)
                .contentType(contentType)
                .bytes(bytes)
                .build());

        if (existing.getLogoBucket() != null && existing.getLogoObjectKey() != null) {
            try {
                fileStorageRepository.delete(existing.getLogoBucket(), existing.getLogoObjectKey());
            } catch (Exception ignored) {
            }
        }

        Instant now = Instant.now();
        SystemConfig updated = SystemConfig.builder()
                .id(existing.getId())
                .brandName(existing.getBrandName())
                .contactEmail(existing.getContactEmail())
                .contactPhone(existing.getContactPhone())
                .cnpj(existing.getCnpj())
                .logoOriginalFileName(originalFileName)
                .logoContentType(contentType)
                .logoSizeBytes((long) bytes.length)
                .logoBucket(DEFAULT_BUCKET)
                .logoObjectKey(objectKey)
                .createdAt(existing.getCreatedAt())
                .updatedAt(now)
                .build();

        return assembler.toResponse(repository.save(updated));
    }

    private void validate(byte[] bytes, String originalFileName, String contentType) {
        if (bytes == null || bytes.length == 0) {
            throw new ValidationException("Arquivo não pode estar vazio.");
        }
        if (bytes.length > MAX_FILE_SIZE) {
            throw new ValidationException("Logomarca excede o tamanho máximo permitido (2MB).");
        }
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new ValidationException("Tipo de arquivo não permitido. Use PNG, JPG ou WEBP.");
        }
        if (originalFileName == null || originalFileName.trim().isBlank()) {
            throw new ValidationException("Nome original do arquivo é obrigatório.");
        }
    }

    private String generateObjectKey(String originalFileName) {
        String uuid = UUID.randomUUID().toString();
        String safeName = originalFileName.replaceAll("[^a-zA-Z0-9.-]", "_");
        return String.format("branding/logo-%s-%s", uuid, safeName);
    }
}
