package br.com.jurispay.infrastructure.persistence.jpa.repository;

import br.com.jurispay.infrastructure.persistence.jpa.entity.UserProfilePreferencesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataUserProfilePreferencesRepository extends JpaRepository<UserProfilePreferencesEntity, String> {
}
