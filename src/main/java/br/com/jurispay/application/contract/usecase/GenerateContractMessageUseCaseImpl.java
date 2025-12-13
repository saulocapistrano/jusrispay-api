package br.com.jurispay.application.contract.usecase;

import br.com.jurispay.application.contract.dto.ContractMessageResponse;
import br.com.jurispay.domain.common.exception.NotFoundException;
import br.com.jurispay.domain.loan.model.Loan;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Implementação do caso de uso para gerar mensagem do contrato.
 */
@Service
public class GenerateContractMessageUseCaseImpl implements GenerateContractMessageUseCase {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final LoanRepository loanRepository;

    public GenerateContractMessageUseCaseImpl(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Override
    public ContractMessageResponse generate(Long loanId) {
        // Buscar empréstimo
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new NotFoundException("Empréstimo não encontrado."));

        // Montar mensagem do contrato
        String message = buildContractMessage(loan);

        return ContractMessageResponse.builder()
                .loanId(loan.getId())
                .customerId(loan.getCustomerId())
                .message(message)
                .generatedAt(Instant.now())
                .build();
    }

    private String buildContractMessage(Loan loan) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Jurispay - Contrato de Empréstimo ===\n\n");
        sb.append("Identificadores:\n");
        sb.append("- Empréstimo ID: ").append(loan.getId()).append("\n");
        sb.append("- Cliente ID: ").append(loan.getCustomerId()).append("\n\n");
        
        sb.append("Condições do Empréstimo:\n");
        sb.append("- Valor Solicitado: R$ ").append(formatCurrency(loan.getValorSolicitado())).append("\n");
        sb.append("- Taxa de Juros: ").append(formatPercent(loan.getTaxaJuros())).append("\n");
        sb.append("- Valor de Devolução Prevista: R$ ").append(formatCurrency(loan.getValorDevolucaoPrevista())).append("\n");
        sb.append("- Multa Diária: R$ ").append(formatCurrency(loan.getMultaDiaria())).append("\n");
        sb.append("- Data de Liberação: ").append(formatDate(loan.getDataLiberacao())).append("\n");
        sb.append("- Data Prevista de Devolução: ").append(formatDate(loan.getDataPrevistaDevolucao())).append("\n");
        
        return sb.toString();
    }

    private String formatCurrency(BigDecimal value) {
        if (value == null) {
            return "0,00";
        }
        return String.format("%.2f", value).replace(".", ",");
    }

    private String formatPercent(BigDecimal value) {
        if (value == null) {
            return "0%";
        }
        return String.format("%.0f%%", value.multiply(BigDecimal.valueOf(100)));
    }

    private String formatDate(Instant instant) {
        if (instant == null) {
            return "N/A";
        }
        return instant.atZone(java.time.ZoneId.systemDefault()).format(DATE_FORMATTER);
    }
}

