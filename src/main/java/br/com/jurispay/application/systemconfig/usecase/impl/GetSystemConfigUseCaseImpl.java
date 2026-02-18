package br.com.jurispay.application.systemconfig.usecase.impl;

import br.com.jurispay.application.systemconfig.assembler.SystemConfigResponseAssembler;
import br.com.jurispay.application.systemconfig.dto.SystemConfigResponse;
import br.com.jurispay.application.systemconfig.service.SystemConfigDefaults;
import br.com.jurispay.application.systemconfig.usecase.GetSystemConfigUseCase;
import br.com.jurispay.domain.systemconfig.repository.SystemConfigRepository;
import org.springframework.stereotype.Service;

@Service
public class GetSystemConfigUseCaseImpl implements GetSystemConfigUseCase {

    private static final Long DEFAULT_ID = 1L;

    private final SystemConfigRepository repository;
    private final SystemConfigResponseAssembler assembler;

    public GetSystemConfigUseCaseImpl(SystemConfigRepository repository, SystemConfigResponseAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @Override
    public SystemConfigResponse get() {
        var config = repository.findById(DEFAULT_ID)
                .orElseGet(() -> repository.save(SystemConfigDefaults.defaultConfig(DEFAULT_ID)));

        return assembler.toResponse(config);
    }
}
