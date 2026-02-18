package br.com.jurispay.infrastructure.persistence.jpa.repository;

import br.com.jurispay.infrastructure.persistence.jpa.entity.LoanTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataLoanTypeRepository extends JpaRepository<LoanTypeEntity, Long> {

    List<LoanTypeEntity> findByAtivoTrue();

    boolean existsByNomeIgnoreCase(String nome);
}
