package br.com.jurispay.application.collection.usecase;

import br.com.jurispay.application.collection.dto.CollectionActionResponse;
import br.com.jurispay.application.collection.mapper.CollectionApplicationMapper;
import br.com.jurispay.domain.common.exception.NotFoundException;
import br.com.jurispay.domain.collection.repository.CollectionRepository;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação do caso de uso de listagem de ações de cobrança por empréstimo.
 */
@Service
public class ListCollectionActionsByLoanUseCaseImpl implements ListCollectionActionsByLoanUseCase {

    private final CollectionRepository collectionRepository;
    private final LoanRepository loanRepository;
    private final CollectionApplicationMapper mapper;

    public ListCollectionActionsByLoanUseCaseImpl(
            CollectionRepository collectionRepository,
            LoanRepository loanRepository,
            CollectionApplicationMapper mapper) {
        this.collectionRepository = collectionRepository;
        this.loanRepository = loanRepository;
        this.mapper = mapper;
    }

    @Override
    public List<CollectionActionResponse> listByLoanId(Long loanId) {
        // Verificar se empréstimo existe
        loanRepository.findById(loanId)
                .orElseThrow(() -> new NotFoundException("Empréstimo não encontrado."));

        // Buscar ações de cobrança
        List<CollectionActionResponse> actions = collectionRepository.findByLoanId(loanId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        return actions;
    }
}

