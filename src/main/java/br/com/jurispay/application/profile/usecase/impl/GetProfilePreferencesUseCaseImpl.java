package br.com.jurispay.application.profile.usecase.impl;

import br.com.jurispay.application.profile.dto.ProfilePreferencesResponse;
import br.com.jurispay.application.profile.usecase.GetProfilePreferencesUseCase;
import br.com.jurispay.domain.userprofile.model.ProfileThemePreference;
import br.com.jurispay.domain.userprofile.model.UserProfilePreferences;
import br.com.jurispay.domain.userprofile.repository.UserProfilePreferencesRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class GetProfilePreferencesUseCaseImpl implements GetProfilePreferencesUseCase {

    private final UserProfilePreferencesRepository repository;

    public GetProfilePreferencesUseCaseImpl(UserProfilePreferencesRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProfilePreferencesResponse getByUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username é obrigatório.");
        }

        UserProfilePreferences preferences = repository.findByUsername(username)
                .orElseGet(() -> repository.save(defaultPreferences(username)));

        return new ProfilePreferencesResponse(
                preferences.getUsername(),
                preferences.getTheme() != null ? preferences.getTheme() : ProfileThemePreference.SYSTEM,
                Boolean.TRUE.equals(preferences.getCompactMode()),
                preferences.getUpdatedAt()
        );
    }

    private UserProfilePreferences defaultPreferences(String username) {
        Instant now = Instant.now();
        return UserProfilePreferences.builder()
                .username(username)
                .theme(ProfileThemePreference.SYSTEM)
                .compactMode(false)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }
}
