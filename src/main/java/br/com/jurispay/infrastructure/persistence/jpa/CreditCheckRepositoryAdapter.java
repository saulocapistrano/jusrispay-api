package br.com.jurispay.infrastructure.persistence.jpa;

import br.com.jurispay.domain.creditcheck.model.CreditCheck;
import br.com.jurispay.domain.creditcheck.model.CreditCheckStatus;
import br.com.jurispay.domain.creditcheck.repository.CreditCheckRepository;
import br.com.jurispay.infrastructure.persistence.jpa.entity.CreditCheckEntity;
import br.com.jurispay.infrastructure.persistence.jpa.repository.SpringDataCreditCheckRepository;
import br.com.jurispay.infrastructure.persistence.mapper.CreditCheckEntityMapper;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class CreditCheckRepositoryAdapter implements CreditCheckRepository {

    private final SpringDataCreditCheckRepository springDataRepository;
    private final CreditCheckEntityMapper mapper;

    public CreditCheckRepositoryAdapter(
            SpringDataCreditCheckRepository springDataRepository,
            CreditCheckEntityMapper mapper) {
        this.springDataRepository = springDataRepository;
        this.mapper = mapper;
    }

    @Override
    public CreditCheck save(CreditCheck creditCheck) {
        CreditCheckEntity entity = mapper.toEntity(creditCheck);
        CreditCheckEntity saved = springDataRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<CreditCheck> findLatestCompletedByCpfSince(String cpf, Instant since) {
        return springDataRepository
                .findFirstByCpfAndStatusAndFinishedAtAfterOrderByFinishedAtDesc(cpf, CreditCheckStatus.COMPLETED, since)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<CreditCheck> findLatestByLoanId(Long loanId) {
        return springDataRepository.findFirstByLoanIdOrderByCreatedAtDesc(loanId)
                .map(mapper::toDomain);
    }
}
