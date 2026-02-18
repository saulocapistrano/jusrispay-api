package br.com.jurispay.api.dto.profile;

import br.com.jurispay.domain.userprofile.model.ProfileThemePreference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProfilePreferencesRequest {

    private ProfileThemePreference theme;
    private Boolean compactMode;
}
