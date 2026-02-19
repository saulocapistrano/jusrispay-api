package br.com.jurispay.application.systemconfig.service;

import br.com.jurispay.domain.systemconfig.model.SystemConfig;

import java.time.Instant;
import java.time.LocalTime;

public final class SystemConfigDefaults {

    private SystemConfigDefaults() {
    }

    public static SystemConfig defaultConfig(Long id) {
        Instant now = Instant.now();
        return SystemConfig.builder()
                .id(id)
                .brandName("Jurispay")
                .notificationTimezone("America/Fortaleza")
                .reminderDispatchTime(LocalTime.of(9, 0))
                .collectionDispatchTime(LocalTime.of(10, 0))
                .createdAt(now)
                .updatedAt(now)
                .build();
    }
}
