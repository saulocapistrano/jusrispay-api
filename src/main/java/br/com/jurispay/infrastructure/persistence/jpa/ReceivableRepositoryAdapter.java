package br.com.jurispay.infrastructure.persistence.jpa;

import br.com.jurispay.domain.receivable.model.Receivable;
import br.com.jurispay.domain.receivable.repository.ReceivableRepository;
import br.com.jurispay.infrastructure.persistence.jpa.entity.LoanEntity;
import br.com.jurispay.infrastructure.persistence.jpa.entity.ReceivableEntity;
import br.com.jurispay.infrastructure.persistence.jpa.repository.SpringDataLoanRepository;
import br.com.jurispay.infrastructure.persistence.jpa.repository.SpringDataReceivableRepository;
import br.com.jurispay.infrastructure.persistence.mapper.ReceivableEntityMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReceivableRepositoryAdapter implements ReceivableRepository {

    private final SpringDataReceivableRepository springDataReceivableRepository;
    private final SpringDataLoanRepository springDataLoanRepository;
    private final ReceivableEntityMapper mapper;

    public ReceivableRepositoryAdapter(
            SpringDataReceivableRepository springDataReceivableRepository,
            SpringDataLoanRepository springDataLoanRepository,
            ReceivableEntityMapper mapper) {
        this.springDataReceivableRepository = springDataReceivableRepository;
        this.springDataLoanRepository = springDataLoanRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Receivable> saveAll(List<Receivable> receivables) {
        if (receivables == null || receivables.isEmpty()) {
            return List.of();
        }

        Long loanId = receivables.get(0).getLoanId();
        LoanEntity loanEntity = springDataLoanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalStateException("Empréstimo não encontrado para persistência de recebíveis."));

        List<ReceivableEntity> entities = receivables.stream()
                .map(r -> mapper.toEntity(r, loanEntity))
                .collect(Collectors.toList());

        return springDataReceivableRepository.saveAll(entities).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Receivable> findByLoanId(Long loanId) {
        return springDataReceivableRepository.findByLoan_IdOrderByInstallmentNumberAsc(loanId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
