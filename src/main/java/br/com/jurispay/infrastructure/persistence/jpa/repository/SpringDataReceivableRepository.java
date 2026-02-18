package br.com.jurispay.infrastructure.persistence.jpa.repository;

import br.com.jurispay.infrastructure.persistence.jpa.entity.ReceivableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataReceivableRepository extends JpaRepository<ReceivableEntity, Long> {

    List<ReceivableEntity> findByLoan_IdOrderByInstallmentNumberAsc(Long loanId);
}
