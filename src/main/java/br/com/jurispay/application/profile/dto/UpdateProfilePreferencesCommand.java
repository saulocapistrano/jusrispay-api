package br.com.jurispay.application.profile.dto;

import br.com.jurispay.domain.userprofile.model.ProfileThemePreference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfilePreferencesCommand {

    private String username;
    private ProfileThemePreference theme;
    private Boolean compactMode;
}
