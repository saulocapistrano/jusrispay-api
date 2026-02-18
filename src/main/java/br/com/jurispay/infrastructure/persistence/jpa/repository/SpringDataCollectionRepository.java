package br.com.jurispay.infrastructure.persistence.jpa.repository;

import br.com.jurispay.infrastructure.persistence.jpa.entity.CollectionActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repositório Spring Data JPA para CollectionActionEntity.
 */
@Repository
public interface SpringDataCollectionRepository extends JpaRepository<CollectionActionEntity, Long> {

    List<CollectionActionEntity> findByLoanIdOrderByActionAtDesc(Long loanId);

    @Query("select max(c.actionAt) from CollectionActionEntity c where c.loanId in :loanIds")
    Optional<Instant> findMaxActionAtByLoanIds(@Param("loanIds") List<Long> loanIds);
}
