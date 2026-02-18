package br.com.jurispay.application.profile.dto;

import br.com.jurispay.domain.userprofile.model.ProfileThemePreference;

import java.time.Instant;

public record ProfilePreferencesResponse(
        String username,
        ProfileThemePreference theme,
        boolean compactMode,
        Instant updatedAt
) {
}
