package br.com.jurispay.application.collection.mapper;

import br.com.jurispay.application.collection.dto.CollectionActionCommand;
import br.com.jurispay.application.collection.dto.CollectionActionResponse;
import br.com.jurispay.domain.collection.model.CollectionAction;
import org.mapstruct.Mapper;

import java.time.Instant;

/**
 * Mapper para conversão entre DTOs de aplicação e modelos de domínio de Collection.
 */
@Mapper(componentModel = "spring")
public interface CollectionApplicationMapper {

    /**
     * Converte comando para modelo de domínio.
     * actionAt e createdAt devem ser fornecidos pelo use case para manter testes determinísticos.
     */
    default CollectionAction toDomain(CollectionActionCommand command, Instant actionAt, Instant createdAt) {
        if (command == null) {
            return null;
        }

        return CollectionAction.builder()
                .loanId(command.getLoanId())
                .actionAt(actionAt)
                .channel(command.getChannel())
                .summary(command.getSummary())
                .outcome(command.getOutcome())
                .createdAt(createdAt)
                .build();
    }

    CollectionActionResponse toResponse(CollectionAction action);
}

