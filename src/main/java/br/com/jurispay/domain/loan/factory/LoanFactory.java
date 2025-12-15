package br.com.jurispay.domain.loan.factory;

import br.com.jurispay.domain.loan.model.Loan;
import br.com.jurispay.domain.loan.model.LoanStatus;
import br.com.jurispay.domain.loan.policy.LoanPolicy;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Factory para criação de Loans.
 * Centraliza a lógica de criação e garante consistência dos valores padrão e calculados.
 */
public class LoanFactory {

    /**
     * Cria um novo Loan com base nos dados fornecidos e política de empréstimo.
     * Calcula valores derivados (valorDevolucaoPrevista) e define valores padrão (taxa, multa, status, datas).
     *
     * @param data dados de criação do empréstimo
     * @param policy política de empréstimo (define taxas e multas)
     * @param now data/hora atual para timestamps
     * @return Loan criado com status OPEN
     */
    public Loan create(LoanCreationData data, LoanPolicy policy, Instant now) {
        BigDecimal taxaJuros = policy.getInterestRate();
        BigDecimal multaDiaria = policy.getDailyFine();

        // Calcular valor de devolução prevista: valorSolicitado * (1 + taxaJuros)
        BigDecimal valorDevolucaoPrevista = data.getValorSolicitado()
                .multiply(BigDecimal.ONE.add(taxaJuros));

        return Loan.builder()
                .customerId(data.getCustomerId())
                .valorSolicitado(data.getValorSolicitado())
                .valorDevolucaoPrevista(valorDevolucaoPrevista)
                .taxaJuros(taxaJuros)
                .multaDiaria(multaDiaria)
                .dataLiberacao(now)
                .dataPrevistaDevolucao(data.getDataPrevistaDevolucao())
                .dataCriacao(now)
                .dataAtualizacao(now)
                .status(LoanStatus.OPEN)
                .build();
    }
}

