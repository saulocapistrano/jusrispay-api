package br.com.jurispay.application.profile.usecase;

import br.com.jurispay.application.profile.dto.ProfilePreferencesResponse;

public interface GetProfilePreferencesUseCase {

    ProfilePreferencesResponse getByUsername(String username);
}
