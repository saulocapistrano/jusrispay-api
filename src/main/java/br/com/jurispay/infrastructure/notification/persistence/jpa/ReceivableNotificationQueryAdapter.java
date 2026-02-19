package br.com.jurispay.infrastructure.notification.persistence.jpa;

import br.com.jurispay.application.notification.port.ReceivableNotificationCandidate;
import br.com.jurispay.application.notification.port.ReceivableNotificationQueryPort;
import br.com.jurispay.domain.receivable.model.ReceivableStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class ReceivableNotificationQueryAdapter implements ReceivableNotificationQueryPort {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ReceivableNotificationCandidate> findPendingDueBetween(Instant startInclusive, Instant endExclusive) {
        return entityManager.createQuery("""
                        select new br.com.jurispay.application.notification.port.ReceivableNotificationCandidate(
                            r.id,
                            l.id,
                            c.id,
                            c.nomeCompleto,
                            c.celular,
                            c.telefone,
                            r.installmentNumber,
                            l.quantidadeParcelas,
                            r.amount,
                            r.dueDate
                        )
                        from br.com.jurispay.infrastructure.persistence.jpa.entity.ReceivableEntity r
                        join r.loan l
                        join l.customer c
                        where r.status = :status
                          and r.dueDate >= :start
                          and r.dueDate < :end
                        order by r.dueDate asc, r.installmentNumber asc
                        """, ReceivableNotificationCandidate.class)
                .setParameter("status", ReceivableStatus.PENDING)
                .setParameter("start", startInclusive)
                .setParameter("end", endExclusive)
                .getResultList();
    }

    @Override
    public List<ReceivableNotificationCandidate> findPendingOverdueBefore(Instant endExclusive) {
        return entityManager.createQuery("""
                        select new br.com.jurispay.application.notification.port.ReceivableNotificationCandidate(
                            r.id,
                            l.id,
                            c.id,
                            c.nomeCompleto,
                            c.celular,
                            c.telefone,
                            r.installmentNumber,
                            l.quantidadeParcelas,
                            r.amount,
                            r.dueDate
                        )
                        from br.com.jurispay.infrastructure.persistence.jpa.entity.ReceivableEntity r
                        join r.loan l
                        join l.customer c
                        where r.status = :status
                          and r.dueDate < :end
                        order by r.dueDate asc, r.installmentNumber asc
                        """, ReceivableNotificationCandidate.class)
                .setParameter("status", ReceivableStatus.PENDING)
                .setParameter("end", endExclusive)
                .getResultList();
    }
}
