package br.com.jurispay.application.systemconfig.usecase.impl;

import br.com.jurispay.application.systemconfig.service.SystemConfigDefaults;
import br.com.jurispay.application.systemconfig.usecase.DownloadSystemLogoUseCase;
import br.com.jurispay.domain.document.repository.FileStorageRepository;
import br.com.jurispay.domain.exception.common.NotFoundException;
import br.com.jurispay.domain.systemconfig.repository.SystemConfigRepository;
import org.springframework.stereotype.Service;

@Service
public class DownloadSystemLogoUseCaseImpl implements DownloadSystemLogoUseCase {

    private static final Long DEFAULT_ID = 1L;

    private final SystemConfigRepository repository;
    private final FileStorageRepository fileStorageRepository;

    public DownloadSystemLogoUseCaseImpl(SystemConfigRepository repository, FileStorageRepository fileStorageRepository) {
        this.repository = repository;
        this.fileStorageRepository = fileStorageRepository;
    }

    @Override
    public DownloadedLogo download() {
        var config = repository.findById(DEFAULT_ID)
                .orElseGet(() -> repository.save(SystemConfigDefaults.defaultConfig(DEFAULT_ID)));

        if (config.getLogoBucket() == null || config.getLogoObjectKey() == null) {
            throw new NotFoundException("Logomarca não configurada.");
        }

        byte[] bytes = fileStorageRepository.getBytes(config.getLogoBucket(), config.getLogoObjectKey());

        String contentType = config.getLogoContentType() != null ? config.getLogoContentType() : "application/octet-stream";
        String fileName = config.getLogoOriginalFileName() != null ? config.getLogoOriginalFileName() : "logo";

        return new DownloadedLogo(bytes, contentType, fileName);
    }
}
