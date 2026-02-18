package br.com.jurispay.domain.userprofile.repository;

import br.com.jurispay.domain.userprofile.model.UserProfilePreferences;

import java.util.Optional;

public interface UserProfilePreferencesRepository {

    Optional<UserProfilePreferences> findByUsername(String username);

    UserProfilePreferences save(UserProfilePreferences preferences);
}
