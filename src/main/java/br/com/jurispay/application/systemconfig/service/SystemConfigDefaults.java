package br.com.jurispay.application.systemconfig.service;

import br.com.jurispay.domain.systemconfig.model.SystemConfig;

import java.time.Instant;

public final class SystemConfigDefaults {

    private SystemConfigDefaults() {
    }

    public static SystemConfig defaultConfig(Long id) {
        Instant now = Instant.now();
        return SystemConfig.builder()
                .id(id)
                .brandName("Jurispay")
                .createdAt(now)
                .updatedAt(now)
                .build();
    }
}
