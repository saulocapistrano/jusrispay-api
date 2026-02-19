package br.com.jurispay.infrastructure.notification.persistence.jpa.repository;

import br.com.jurispay.infrastructure.notification.persistence.jpa.entity.NotificationTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface SpringDataNotificationTemplateRepository extends JpaRepository<NotificationTemplateEntity, Long> {

    Optional<NotificationTemplateEntity> findByCodeAndActiveIsTrue(String code);

    List<NotificationTemplateEntity> findAllByOrderByCodeAsc();

    Optional<NotificationTemplateEntity> findByCode(String code);
}
