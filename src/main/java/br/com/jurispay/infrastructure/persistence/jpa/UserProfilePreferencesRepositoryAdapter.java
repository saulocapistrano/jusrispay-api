package br.com.jurispay.infrastructure.persistence.jpa;

import br.com.jurispay.domain.userprofile.model.UserProfilePreferences;
import br.com.jurispay.domain.userprofile.repository.UserProfilePreferencesRepository;
import br.com.jurispay.infrastructure.persistence.jpa.repository.SpringDataUserProfilePreferencesRepository;
import br.com.jurispay.infrastructure.persistence.mapper.UserProfilePreferencesEntityMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserProfilePreferencesRepositoryAdapter implements UserProfilePreferencesRepository {

    private final SpringDataUserProfilePreferencesRepository repository;
    private final UserProfilePreferencesEntityMapper mapper;

    public UserProfilePreferencesRepositoryAdapter(
            SpringDataUserProfilePreferencesRepository repository,
            UserProfilePreferencesEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<UserProfilePreferences> findByUsername(String username) {
        if (username == null || username.isBlank()) {
            return Optional.empty();
        }
        return repository.findById(username).map(mapper::toDomain);
    }

    @Override
    public UserProfilePreferences save(UserProfilePreferences preferences) {
        var saved = repository.save(mapper.toEntity(preferences));
        return mapper.toDomain(saved);
    }
}
