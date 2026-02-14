package br.com.jurispay.infrastructure.persistence.jpa;

import br.com.jurispay.domain.creditanalysis.model.CreditAnalysis;
import br.com.jurispay.domain.creditanalysis.repository.CreditAnalysisRepository;
import br.com.jurispay.infrastructure.persistence.jpa.entity.CreditAnalysisEntity;
import br.com.jurispay.infrastructure.persistence.jpa.repository.SpringDataCreditAnalysisRepository;
import br.com.jurispay.infrastructure.persistence.mapper.CreditAnalysisEntityMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador que implementa CreditAnalysisRepository do domínio usando JPA.
 * Converte entre entidades JPA e modelos de domínio.
 */
@Component
public class CreditAnalysisRepositoryAdapter implements CreditAnalysisRepository {

    private final SpringDataCreditAnalysisRepository springDataCreditAnalysisRepository;
    private final CreditAnalysisEntityMapper mapper;

    public CreditAnalysisRepositoryAdapter(
            SpringDataCreditAnalysisRepository springDataCreditAnalysisRepository,
            CreditAnalysisEntityMapper mapper) {
        this.springDataCreditAnalysisRepository = springDataCreditAnalysisRepository;
        this.mapper = mapper;
    }

    @Override
    public CreditAnalysis save(CreditAnalysis analysis) {
        CreditAnalysisEntity entity = mapper.toEntity(analysis);
        CreditAnalysisEntity savedEntity = springDataCreditAnalysisRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<CreditAnalysis> findById(Long id) {
        return springDataCreditAnalysisRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<CreditAnalysis> findByCustomerId(Long customerId) {
        return springDataCreditAnalysisRepository.findByCustomerId(customerId)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<CreditAnalysis> findByLoanId(Long loanId) {
        return springDataCreditAnalysisRepository.findByLoanId(loanId)
                .map(mapper::toDomain);
    }

    @Override
    public List<CreditAnalysis> findAll() {
        return springDataCreditAnalysisRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}

