package br.com.jurispay.application.profile.usecase;

import br.com.jurispay.application.profile.dto.ProfilePreferencesResponse;
import br.com.jurispay.application.profile.dto.UpdateProfilePreferencesCommand;

public interface UpdateProfilePreferencesUseCase {

    ProfilePreferencesResponse update(UpdateProfilePreferencesCommand command);
}
