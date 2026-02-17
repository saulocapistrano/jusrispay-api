package br.com.jurispay.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "credit_decision_override")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditDecisionOverrideEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "loan_id", nullable = false)
    private Long loanId;

    @Column(name = "credit_check_id")
    private Long creditCheckId;

    @Column(name = "override_by_user_id", nullable = false)
    private Long overrideByUserId;

    @Column(name = "override_reason", nullable = false, columnDefinition = "TEXT")
    private String overrideReason;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}
