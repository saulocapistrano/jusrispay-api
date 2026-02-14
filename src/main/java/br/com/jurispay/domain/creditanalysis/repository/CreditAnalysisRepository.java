package br.com.jurispay.domain.creditanalysis.repository;

import br.com.jurispay.domain.creditanalysis.model.CreditAnalysis;

import java.util.List;
import java.util.Optional;

/**
 * Porta de repositório para CreditAnalysis.
 * Define as operações de persistência de análises de crédito.
 */
public interface CreditAnalysisRepository {

    /**
     * Salva ou atualiza uma análise de crédito.
     *
     * @param analysis análise a ser salva
     * @return análise salva (com ID preenchido se for novo)
     */
    CreditAnalysis save(CreditAnalysis analysis);

    /**
     * Busca uma análise por ID.
     *
     * @param id ID da análise
     * @return análise encontrada ou Optional.empty()
     */
    Optional<CreditAnalysis> findById(Long id);

    /**
     * Busca análise por ID do empréstimo.
     *
     * @param loanId ID do empréstimo
     * @return análise encontrada ou Optional.empty()
     */
    Optional<CreditAnalysis> findByLoanId(Long loanId);

    /**
     * Busca análise por ID do cliente.
     *
     * @param customerId ID do cliente
     * @return análise encontrada ou Optional.empty()
     */
    Optional<CreditAnalysis> findByCustomerId(Long customerId);

    /**
     * Lista todas as análises.
     *
     * @return lista de análises
     */
    List<CreditAnalysis> findAll();
}

