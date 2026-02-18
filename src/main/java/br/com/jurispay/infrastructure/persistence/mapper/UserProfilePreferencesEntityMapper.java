package br.com.jurispay.infrastructure.persistence.mapper;

import br.com.jurispay.domain.userprofile.model.ProfileThemePreference;
import br.com.jurispay.domain.userprofile.model.UserProfilePreferences;
import br.com.jurispay.infrastructure.persistence.jpa.entity.UserProfilePreferencesEntity;
import org.springframework.stereotype.Component;

@Component
public class UserProfilePreferencesEntityMapper {

    public UserProfilePreferences toDomain(UserProfilePreferencesEntity entity) {
        if (entity == null) {
            return null;
        }

        ProfileThemePreference theme = parseTheme(entity.getTheme());

        return UserProfilePreferences.builder()
                .username(entity.getUsername())
                .theme(theme)
                .compactMode(entity.getCompactMode())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public UserProfilePreferencesEntity toEntity(UserProfilePreferences domain) {
        if (domain == null) {
            return null;
        }

        return UserProfilePreferencesEntity.builder()
                .username(domain.getUsername())
                .theme(domain.getTheme() != null ? domain.getTheme().name() : null)
                .compactMode(domain.getCompactMode())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }

    private ProfileThemePreference parseTheme(String raw) {
        if (raw == null || raw.isBlank()) {
            return ProfileThemePreference.SYSTEM;
        }

        try {
            return ProfileThemePreference.valueOf(raw.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return ProfileThemePreference.SYSTEM;
        }
    }
}
