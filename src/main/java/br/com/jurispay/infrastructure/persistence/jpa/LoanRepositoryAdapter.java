package br.com.jurispay.infrastructure.persistence.jpa;

import br.com.jurispay.domain.loan.model.Loan;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import br.com.jurispay.infrastructure.persistence.jpa.entity.CustomerEntity;
import br.com.jurispay.infrastructure.persistence.jpa.entity.LoanEntity;
import br.com.jurispay.infrastructure.persistence.jpa.repository.SpringDataCustomerRepository;
import br.com.jurispay.infrastructure.persistence.jpa.repository.SpringDataLoanRepository;
import br.com.jurispay.infrastructure.persistence.mapper.LoanEntityMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador que implementa LoanRepository do domínio usando JPA.
 * Converte entre entidades JPA e modelos de domínio.
 */
@Component
public class LoanRepositoryAdapter implements LoanRepository {

    private final SpringDataLoanRepository springDataLoanRepository;
    private final SpringDataCustomerRepository springDataCustomerRepository;
    private final LoanEntityMapper mapper;

    public LoanRepositoryAdapter(
            SpringDataLoanRepository springDataLoanRepository,
            SpringDataCustomerRepository springDataCustomerRepository,
            LoanEntityMapper mapper) {
        this.springDataLoanRepository = springDataLoanRepository;
        this.springDataCustomerRepository = springDataCustomerRepository;
        this.mapper = mapper;
    }

    @Override
    public Loan save(Loan loan) {
        // Carregar CustomerEntity pelo ID
        CustomerEntity customerEntity = springDataCustomerRepository.findById(loan.getCustomerId())
                .orElseThrow(() -> new IllegalStateException("Cliente não encontrado para persistência do empréstimo."));

        // Converter Loan -> LoanEntity
        LoanEntity entity = mapper.toEntity(loan, customerEntity);

        // Salvar
        LoanEntity savedEntity = springDataLoanRepository.save(entity);

        // Converter de volta para domínio
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Loan> findById(Long id) {
        return springDataLoanRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Loan> findAll() {
        return springDataLoanRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}

