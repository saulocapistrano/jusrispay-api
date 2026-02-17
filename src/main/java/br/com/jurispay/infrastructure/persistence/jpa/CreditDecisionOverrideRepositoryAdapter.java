package br.com.jurispay.infrastructure.persistence.jpa;

import br.com.jurispay.domain.creditdecisionoverride.model.CreditDecisionOverride;
import br.com.jurispay.domain.creditdecisionoverride.repository.CreditDecisionOverrideRepository;
import br.com.jurispay.infrastructure.persistence.jpa.entity.CreditDecisionOverrideEntity;
import br.com.jurispay.infrastructure.persistence.jpa.repository.SpringDataCreditDecisionOverrideRepository;
import br.com.jurispay.infrastructure.persistence.mapper.CreditDecisionOverrideEntityMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CreditDecisionOverrideRepositoryAdapter implements CreditDecisionOverrideRepository {

    private final SpringDataCreditDecisionOverrideRepository springDataRepository;
    private final CreditDecisionOverrideEntityMapper mapper;

    public CreditDecisionOverrideRepositoryAdapter(
            SpringDataCreditDecisionOverrideRepository springDataRepository,
            CreditDecisionOverrideEntityMapper mapper) {
        this.springDataRepository = springDataRepository;
        this.mapper = mapper;
    }

    @Override
    public CreditDecisionOverride save(CreditDecisionOverride override) {
        CreditDecisionOverrideEntity entity = mapper.toEntity(override);
        CreditDecisionOverrideEntity saved = springDataRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<CreditDecisionOverride> findLatestByLoanId(Long loanId) {
        return springDataRepository.findFirstByLoanIdOrderByCreatedAtDesc(loanId)
                .map(mapper::toDomain);
    }
}
