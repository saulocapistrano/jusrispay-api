package br.com.jurispay.application.collection.usecase;

import br.com.jurispay.application.collection.dto.CollectionActionCommand;
import br.com.jurispay.application.collection.dto.CollectionActionResponse;
import br.com.jurispay.application.collection.mapper.CollectionApplicationMapper;
import br.com.jurispay.domain.common.exception.NotFoundException;
import br.com.jurispay.domain.common.exception.ValidationException;
import br.com.jurispay.domain.collection.model.CollectionAction;
import br.com.jurispay.domain.collection.repository.CollectionRepository;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Implementação do caso de uso de registro de ação de cobrança.
 */
@Service
public class RegisterCollectionActionUseCaseImpl implements RegisterCollectionActionUseCase {

    private static final int MAX_SUMMARY_LENGTH = 300;
    private static final int MAX_OUTCOME_LENGTH = 60;

    private final CollectionRepository collectionRepository;
    private final LoanRepository loanRepository;
    private final CollectionApplicationMapper mapper;

    public RegisterCollectionActionUseCaseImpl(
            CollectionRepository collectionRepository,
            LoanRepository loanRepository,
            CollectionApplicationMapper mapper) {
        this.collectionRepository = collectionRepository;
        this.loanRepository = loanRepository;
        this.mapper = mapper;
    }

    @Override
    public CollectionActionResponse register(CollectionActionCommand command) {
        // Validações básicas
        validateCommand(command);

        // Verificar se empréstimo existe
        loanRepository.findById(command.getLoanId())
                .orElseThrow(() -> new NotFoundException("Empréstimo não encontrado para registro de cobrança."));

        // Criar timestamps
        Instant now = Instant.now();
        Instant actionAt = now;
        Instant createdAt = now;

        // Converter comando para domínio
        CollectionAction action = mapper.toDomain(command, actionAt, createdAt);

        // Salvar ação
        CollectionAction savedAction = collectionRepository.save(action);

        // Retornar resposta
        return mapper.toResponse(savedAction);
    }

    private void validateCommand(CollectionActionCommand command) {
        if (command.getLoanId() == null) {
            throw new ValidationException("ID do empréstimo é obrigatório.");
        }

        if (command.getChannel() == null) {
            throw new ValidationException("Canal de cobrança é obrigatório.");
        }

        if (command.getSummary() == null || command.getSummary().trim().isEmpty()) {
            throw new ValidationException("Resumo da ação é obrigatório.");
        }

        if (command.getSummary().length() > MAX_SUMMARY_LENGTH) {
            throw new ValidationException("Resumo da ação não pode exceder " + MAX_SUMMARY_LENGTH + " caracteres.");
        }

        if (command.getOutcome() != null && command.getOutcome().length() > MAX_OUTCOME_LENGTH) {
            throw new ValidationException("Resultado da ação não pode exceder " + MAX_OUTCOME_LENGTH + " caracteres.");
        }
    }
}

