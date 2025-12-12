package br.com.jurispay.application.document.usecase;

import br.com.jurispay.application.document.dto.DocumentResponse;

import java.util.List;

/**
 * Use case para listar documentos.
 */
public interface ListDocumentsUseCase {

    /**
     * Lista todos os documentos.
     *
     * @return lista de documentos
     */
    List<DocumentResponse> listAll();

    /**
     * Lista documentos por ID do cliente.
     *
     * @param customerId ID do cliente
     * @return lista de documentos do cliente
     */
    List<DocumentResponse> listByCustomerId(Long customerId);

    /**
     * Lista documentos por ID do empréstimo.
     *
     * @param loanId ID do empréstimo
     * @return lista de documentos do empréstimo
     */
    List<DocumentResponse> listByLoanId(Long loanId);
}

