package br.com.jurispay.application.profile.usecase.impl;

import br.com.jurispay.application.profile.dto.ProfilePreferencesResponse;
import br.com.jurispay.application.profile.dto.UpdateProfilePreferencesCommand;
import br.com.jurispay.application.profile.usecase.UpdateProfilePreferencesUseCase;
import br.com.jurispay.domain.userprofile.model.ProfileThemePreference;
import br.com.jurispay.domain.userprofile.model.UserProfilePreferences;
import br.com.jurispay.domain.userprofile.repository.UserProfilePreferencesRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UpdateProfilePreferencesUseCaseImpl implements UpdateProfilePreferencesUseCase {

    private final UserProfilePreferencesRepository repository;

    public UpdateProfilePreferencesUseCaseImpl(UserProfilePreferencesRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProfilePreferencesResponse update(UpdateProfilePreferencesCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command é obrigatório.");
        }
        if (command.getUsername() == null || command.getUsername().isBlank()) {
            throw new IllegalArgumentException("username é obrigatório.");
        }

        UserProfilePreferences existing = repository.findByUsername(command.getUsername())
                .orElseGet(() -> repository.save(defaultPreferences(command.getUsername())));

        Instant now = Instant.now();
        ProfileThemePreference theme = command.getTheme() != null
                ? command.getTheme()
                : (existing.getTheme() != null ? existing.getTheme() : ProfileThemePreference.SYSTEM);

        boolean compactMode = command.getCompactMode() != null
                ? command.getCompactMode()
                : Boolean.TRUE.equals(existing.getCompactMode());

        UserProfilePreferences updated = UserProfilePreferences.builder()
                .username(existing.getUsername())
                .theme(theme)
                .compactMode(compactMode)
                .createdAt(existing.getCreatedAt())
                .updatedAt(now)
                .build();

        UserProfilePreferences saved = repository.save(updated);

        return new ProfilePreferencesResponse(
                saved.getUsername(),
                saved.getTheme() != null ? saved.getTheme() : ProfileThemePreference.SYSTEM,
                Boolean.TRUE.equals(saved.getCompactMode()),
                saved.getUpdatedAt()
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
