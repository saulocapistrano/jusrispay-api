package br.com.jurispay.infrastructure.notification.persistence.jpa.repository;

import br.com.jurispay.infrastructure.notification.persistence.jpa.entity.NotificationOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataNotificationOutboxRepository extends JpaRepository<NotificationOutboxEntity, Long> {

    boolean existsByDedupKey(String dedupKey);

    List<NotificationOutboxEntity> findTop200ByStatusOrderByCreatedAtDesc(String status);

    List<NotificationOutboxEntity> findTop200ByStatusInOrderByCreatedAtDesc(List<String> statuses);

    List<NotificationOutboxEntity> findTop200ByReceivableIdOrderByCreatedAtDesc(Long receivableId);

    List<NotificationOutboxEntity> findTop200ByLoanIdOrderByCreatedAtDesc(Long loanId);

    List<NotificationOutboxEntity> findTop200ByCustomerIdOrderByCreatedAtDesc(Long customerId);

    List<NotificationOutboxEntity> findTop200ByOrderByCreatedAtDesc();

    Optional<NotificationOutboxEntity> findById(Long id);

    @Query("""
            select o from NotificationOutboxEntity o
            where o.status in ('PENDING','RETRY')
              and (o.nextAttemptAt is null or o.nextAttemptAt <= :now)
            order by o.createdAt asc
            """)
    List<NotificationOutboxEntity> findDispatchCandidates(@Param("now") Instant now);
}
