package br.com.jurispay.application.notification.port;

import java.time.Instant;
import java.util.List;

public interface ReceivableNotificationQueryPort {

    List<ReceivableNotificationCandidate> findPendingDueBetween(Instant startInclusive, Instant endExclusive);

    List<ReceivableNotificationCandidate> findPendingOverdueBefore(Instant endExclusive);
}
