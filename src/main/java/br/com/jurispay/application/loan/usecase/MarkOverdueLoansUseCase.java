package br.com.jurispay.application.loan.usecase;

/**
 * Caso de uso para marcar empréstimos em atraso como OVERDUE.
 */
public interface MarkOverdueLoansUseCase {

    /**
     * Marca empréstimos abertos com data de devolução prevista no passado como OVERDUE.
     *
     * @return número de empréstimos atualizados
     */
    int markOverdue();
}

