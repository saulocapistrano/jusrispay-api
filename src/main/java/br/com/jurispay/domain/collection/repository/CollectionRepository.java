package br.com.jurispay.domain.collection.repository;

import br.com.jurispay.domain.collection.model.CollectionAction;

import java.util.List;
import java.util.Optional;

/**
 * Interface de repositório do domínio CollectionAction.
 * Define as operações de persistência sem depender de implementação específica.
 */
public interface CollectionRepository {

    /**
     * Salva ou atualiza uma ação de cobrança.
     *
     * @param action ação a ser salva
     * @return ação salva (com ID preenchido se for novo)
     */
    CollectionAction save(CollectionAction action);

    /**
     * Busca uma ação por ID.
     *
     * @param id ID da ação
     * @return ação encontrada ou Optional.empty()
     */
    Optional<CollectionAction> findById(Long id);

    /**
     * Busca todas as ações de cobrança de um empréstimo.
     *
     * @param loanId ID do empréstimo
     * @return lista de ações de cobrança
     */
    List<CollectionAction> findByLoanId(Long loanId);
}

