package br.com.jurispay.infrastructure.persistence.jpa;

import br.com.jurispay.domain.loantype.model.LoanType;
import br.com.jurispay.domain.loantype.repository.LoanTypeRepository;
import br.com.jurispay.infrastructure.persistence.jpa.entity.LoanTypeEntity;
import br.com.jurispay.infrastructure.persistence.jpa.repository.SpringDataLoanTypeRepository;
import br.com.jurispay.infrastructure.persistence.mapper.LoanTypeEntityMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class LoanTypeRepositoryAdapter implements LoanTypeRepository {

    private final SpringDataLoanTypeRepository repository;
    private final LoanTypeEntityMapper mapper;

    public LoanTypeRepositoryAdapter(SpringDataLoanTypeRepository repository, LoanTypeEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public LoanType save(LoanType loanType) {
        LoanTypeEntity saved = repository.save(mapper.toEntity(loanType));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<LoanType> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<LoanType> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<LoanType> findAllActive() {
        return repository.findByAtivoTrue().stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existsByNomeIgnoreCase(String nome) {
        return repository.existsByNomeIgnoreCase(nome);
    }
}
