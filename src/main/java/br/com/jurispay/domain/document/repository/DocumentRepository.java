package br.com.jurispay.domain.document.repository;

import br.com.jurispay.domain.document.model.Document;

import java.util.List;
import java.util.Optional;

/**
 * Porta de repositório para Document.
 * Define as operações de persistência de documentos.
 */
public interface DocumentRepository {

    /**
     * Salva ou atualiza um documento.
     *
     * @param document documento a ser salvo
     * @return documento salvo (com ID preenchido se for novo)
     */
    Document save(Document document);

    /**
     * Busca um documento por ID.
     *
     * @param id ID do documento
     * @return documento encontrado ou Optional.empty()
     */
    Optional<Document> findById(Long id);

    /**
     * Lista todos os documentos.
     *
     * @return lista de documentos
     */
    List<Document> findAll();

    /**
     * Busca documentos por ID do cliente.
     *
     * @param customerId ID do cliente
     * @return lista de documentos do cliente
     */
    List<Document> findByCustomerId(Long customerId);

    /**
     * Busca documentos por ID do empréstimo.
     *
     * @param loanId ID do empréstimo
     * @return lista de documentos do empréstimo
     */
    List<Document> findByLoanId(Long loanId);
}

