package br.com.jurispay.domain.userprofile.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfilePreferences {

    private String username;
    private ProfileThemePreference theme;
    private Boolean compactMode;
    private Instant createdAt;
    private Instant updatedAt;
}
