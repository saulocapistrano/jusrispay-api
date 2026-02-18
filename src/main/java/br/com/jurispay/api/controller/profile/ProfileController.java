package br.com.jurispay.api.controller.profile;

import br.com.jurispay.api.dto.profile.UpdateProfilePreferencesRequest;
import br.com.jurispay.application.profile.dto.ProfilePreferencesResponse;
import br.com.jurispay.application.profile.dto.UpdateProfilePreferencesCommand;
import br.com.jurispay.application.profile.usecase.GetProfilePreferencesUseCase;
import br.com.jurispay.application.profile.usecase.UpdateProfilePreferencesUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final GetProfilePreferencesUseCase getProfilePreferencesUseCase;
    private final UpdateProfilePreferencesUseCase updateProfilePreferencesUseCase;

    public ProfileController(
            GetProfilePreferencesUseCase getProfilePreferencesUseCase,
            UpdateProfilePreferencesUseCase updateProfilePreferencesUseCase) {
        this.getProfilePreferencesUseCase = getProfilePreferencesUseCase;
        this.updateProfilePreferencesUseCase = updateProfilePreferencesUseCase;
    }

    @GetMapping("/preferences")
    public ResponseEntity<ProfilePreferencesResponse> getPreferences(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(getProfilePreferencesUseCase.getByUsername(username));
    }

    @PutMapping("/preferences")
    public ResponseEntity<ProfilePreferencesResponse> updatePreferences(
            Authentication authentication,
            @RequestBody UpdateProfilePreferencesRequest request) {
        String username = authentication.getName();

        UpdateProfilePreferencesCommand command = UpdateProfilePreferencesCommand.builder()
                .username(username)
                .theme(request != null ? request.getTheme() : null)
                .compactMode(request != null ? request.getCompactMode() : null)
                .build();

        return ResponseEntity.ok(updateProfilePreferencesUseCase.update(command));
    }
}
