package br.com.jurispay.infrastructure.persistence.report;

import br.com.jurispay.domain.collection.model.OverdueInfo;
import br.com.jurispay.domain.collection.service.OverdueCalculator;
import br.com.jurispay.domain.loan.model.LoanStatus;
import br.com.jurispay.domain.loan.service.RoiCalculator;
import br.com.jurispay.domain.report.model.DueLoanItem;
import br.com.jurispay.domain.report.model.OverdueLoanItem;
import br.com.jurispay.domain.report.model.PortfolioSummary;
import br.com.jurispay.domain.report.repository.ReportRepository;
import br.com.jurispay.infrastructure.persistence.jpa.entity.LoanEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Adaptador que implementa ReportRepository do domínio usando JPA/EntityManager.
 * Executa queries agregadas para relatórios.
 */
@Component
public class ReportRepositoryAdapter implements ReportRepository {

    private final EntityManager entityManager;
    private final OverdueCalculator overdueCalculator;
    private final RoiCalculator roiCalculator;

    public ReportRepositoryAdapter(
            EntityManager entityManager,
            OverdueCalculator overdueCalculator,
            RoiCalculator roiCalculator) {
        this.entityManager = entityManager;
        this.overdueCalculator = overdueCalculator;
        this.roiCalculator = roiCalculator;
    }

    @Override
    public PortfolioSummary getPortfolioSummary(Instant now) {
        // Total emprestado (soma de valorSolicitado)
        BigDecimal totalLoaned = getTotalLoaned();

        // Total esperado (soma de valorDevolucaoPrevista)
        BigDecimal totalExpected = getTotalExpected();

        // Total recebido (soma de valorPago dos payments)
        BigDecimal totalReceived = getTotalReceived();

        // Lucro total
        BigDecimal totalProfit = totalReceived.subtract(totalLoaned);

        // ROI percentual usando RoiCalculator
        BigDecimal roiPercent = roiCalculator.calculateRoiPercent(totalProfit, totalLoaned);

        // Contagens por status
        Long openLoans = countLoansByStatus(LoanStatus.OPEN) + countLoansByStatus(LoanStatus.CREDITED);
        Long overdueLoans = countLoansByStatus(LoanStatus.OVERDUE);
        Long paidLoans = countLoansByStatus(LoanStatus.PAID);

        return PortfolioSummary.builder()
                .totalLoaned(totalLoaned != null ? totalLoaned : BigDecimal.ZERO)
                .totalExpected(totalExpected != null ? totalExpected : BigDecimal.ZERO)
                .totalReceived(totalReceived != null ? totalReceived : BigDecimal.ZERO)
                .totalProfit(totalProfit)
                .roiPercent(roiPercent)
                .openLoans(openLoans)
                .overdueLoans(overdueLoans)
                .paidLoans(paidLoans)
                .generatedAt(now)
                .build();
    }

    @Override
    public List<DueLoanItem> listNextDueLoans(Instant now, int limit) {
        String jpql = "SELECT l FROM LoanEntity l " +
                "WHERE (l.status = :openStatus OR l.status = :creditedStatus) " +
                "AND l.dataPrevistaDevolucao >= :now " +
                "ORDER BY l.dataPrevistaDevolucao ASC";

        TypedQuery<LoanEntity> query = entityManager.createQuery(jpql, LoanEntity.class);
        query.setParameter("openStatus", LoanStatus.OPEN);
        query.setParameter("creditedStatus", LoanStatus.CREDITED);
        query.setParameter("now", now);
        query.setMaxResults(limit);

        return query.getResultList().stream()
                .map(this::toDueLoanItem)
                .collect(Collectors.toList());
    }

    @Override
    public List<OverdueLoanItem> listOverdueLoans(Instant now, int limit) {
        String jpql = "SELECT l FROM LoanEntity l " +
                "WHERE (l.status = :overdueStatus OR ((l.status = :openStatus OR l.status = :creditedStatus) AND l.dataPrevistaDevolucao < :now)) " +
                "ORDER BY l.dataPrevistaDevolucao ASC";

        TypedQuery<LoanEntity> query = entityManager.createQuery(jpql, LoanEntity.class);
        query.setParameter("overdueStatus", LoanStatus.OVERDUE);
        query.setParameter("openStatus", LoanStatus.OPEN);
        query.setParameter("creditedStatus", LoanStatus.CREDITED);
        query.setParameter("now", now);
        query.setMaxResults(limit);

        return query.getResultList().stream()
                .map(loanEntity -> toOverdueLoanItem(loanEntity, now))
                .collect(Collectors.toList());
    }

    private BigDecimal getTotalLoaned() {
        String jpql = "SELECT COALESCE(SUM(l.valorSolicitado), 0) FROM LoanEntity l";
        TypedQuery<BigDecimal> query = entityManager.createQuery(jpql, BigDecimal.class);
        BigDecimal result = query.getSingleResult();
        return result != null ? result : BigDecimal.ZERO;
    }

    private BigDecimal getTotalExpected() {
        String jpql = "SELECT COALESCE(SUM(l.valorDevolucaoPrevista), 0) FROM LoanEntity l";
        TypedQuery<BigDecimal> query = entityManager.createQuery(jpql, BigDecimal.class);
        BigDecimal result = query.getSingleResult();
        return result != null ? result : BigDecimal.ZERO;
    }

    private BigDecimal getTotalReceived() {
        String jpql = "SELECT COALESCE(SUM(p.valorPago), 0) FROM PaymentEntity p";
        TypedQuery<BigDecimal> query = entityManager.createQuery(jpql, BigDecimal.class);
        BigDecimal result = query.getSingleResult();
        return result != null ? result : BigDecimal.ZERO;
    }

    private Long countLoansByStatus(LoanStatus status) {
        String jpql = "SELECT COUNT(l) FROM LoanEntity l WHERE l.status = :status";
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        query.setParameter("status", status);
        return query.getSingleResult();
    }


    private DueLoanItem toDueLoanItem(LoanEntity entity) {
        return DueLoanItem.builder()
                .loanId(entity.getId())
                .customerId(entity.getCustomer().getId())
                .dueDate(entity.getDataPrevistaDevolucao())
                .expectedAmount(entity.getValorDevolucaoPrevista())
                .status(entity.getStatus())
                .build();
    }

    private OverdueLoanItem toOverdueLoanItem(LoanEntity entity, Instant now) {
        // Calcular multa usando OverdueCalculator
        OverdueInfo overdueInfo = overdueCalculator.calculate(
                entity.getDataPrevistaDevolucao(),
                now,
                entity.getMultaDiaria()
        );

        // Valor esperado incluindo multa
        BigDecimal expectedAmount = entity.getValorDevolucaoPrevista().add(overdueInfo.getTotalFine());

        return OverdueLoanItem.builder()
                .loanId(entity.getId())
                .customerId(entity.getCustomer().getId())
                .dueDate(entity.getDataPrevistaDevolucao())
                .daysOverdue(overdueInfo.getDaysOverdue())
                .dailyFine(entity.getMultaDiaria())
                .totalFine(overdueInfo.getTotalFine())
                .expectedAmount(expectedAmount)
                .build();
    }
}

