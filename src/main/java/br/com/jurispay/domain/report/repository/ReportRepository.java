package br.com.jurispay.domain.report.repository;

import br.com.jurispay.domain.report.model.DueLoanItem;
import br.com.jurispay.domain.report.model.OverdueLoanItem;
import br.com.jurispay.domain.report.model.PortfolioSummary;

import java.time.Instant;
import java.util.List;

/**
 * Interface de repositório para consultas de relatórios.
 * Define operações de consulta agregada sem depender de implementação específica.
 */
public interface ReportRepository {

    /**
     * Obtém resumo do portfólio de empréstimos.
     *
     * @param now data/hora atual para cálculos
     * @return resumo do portfólio
     */
    PortfolioSummary getPortfolioSummary(Instant now);

    /**
     * Lista empréstimos com vencimento próximo.
     *
     * @param now data/hora atual para comparação
     * @param limit quantidade máxima de itens a retornar
     * @return lista de empréstimos com vencimento próximo
     */
    List<DueLoanItem> listNextDueLoans(Instant now, int limit);

    /**
     * Lista empréstimos em atraso.
     *
     * @param now data/hora atual para cálculo de dias em atraso
     * @param limit quantidade máxima de itens a retornar
     * @return lista de empréstimos em atraso
     */
    List<OverdueLoanItem> listOverdueLoans(Instant now, int limit);
}

