package br.com.jurispay.infrastructure.contract.pdf;

import br.com.jurispay.domain.loan.model.Loan;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Renderizador de PDF para contratos de empréstimo.
 */
@Component
public class ContractPdfRenderer {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

    /**
     * Gera um PDF simples com as condições do empréstimo.
     *
     * @param loan empréstimo para gerar o contrato
     * @return bytes do PDF gerado
     */
    public byte[] render(Loan loan) {
        try {
            Document document = new Document(PageSize.A4);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, outputStream);

            document.open();

            // Título
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Jurispay - Contrato de Empréstimo", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Identificadores
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Paragraph identifiersTitle = new Paragraph("Identificadores:", sectionFont);
            identifiersTitle.setSpacingAfter(10);
            document.add(identifiersTitle);

            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            document.add(new Paragraph("Empréstimo ID: " + loan.getId(), normalFont));
            document.add(new Paragraph("Cliente ID: " + loan.getCustomerId(), normalFont));
            document.add(new Paragraph(" ")); // Espaço

            // Condições
            Paragraph conditionsTitle = new Paragraph("Condições do Empréstimo:", sectionFont);
            conditionsTitle.setSpacingAfter(10);
            document.add(conditionsTitle);

            document.add(new Paragraph("Valor Solicitado: R$ " + formatCurrency(loan.getValorSolicitado()), normalFont));
            document.add(new Paragraph("Taxa de Juros: " + formatPercent(loan.getTaxaJuros()), normalFont));
            document.add(new Paragraph("Valor de Devolução Prevista: R$ " + formatCurrency(loan.getValorDevolucaoPrevista()), normalFont));
            document.add(new Paragraph("Multa Diária: R$ " + formatCurrency(loan.getMultaDiaria()) + "/dia", normalFont));
            document.add(new Paragraph("Data de Liberação: " + formatDate(loan.getDataLiberacao()), normalFont));
            document.add(new Paragraph("Data Prevista de Devolução: " + formatDate(loan.getDataPrevistaDevolucao()), normalFont));

            // Rodapé
            document.add(new Paragraph(" ")); // Espaço
            Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 9);
            Paragraph footer = new Paragraph("Documento gerado automaticamente pelo sistema.", footerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(30);
            document.add(footer);

            document.close();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF do contrato: " + e.getMessage(), e);
        }
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
        return DATE_FORMATTER.format(instant);
    }
}

