package br.com.jurispay.infrastructure.persistence.jpa.entity;

import br.com.jurispay.domain.collection.model.CollectionChannel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Entidade JPA para CollectionAction.
 * Representa a tabela collection_action no banco de dados.
 */
@Entity
@Table(name = "collection_action")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectionActionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "loan_id", nullable = false)
    private Long loanId;

    @Column(name = "action_at", nullable = false)
    private Instant actionAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false, length = 30)
    private CollectionChannel channel;

    @Column(name = "summary", nullable = false, length = 300)
    private String summary;

    @Column(name = "outcome", length = 60)
    private String outcome;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (actionAt == null) {
            actionAt = now;
        }
    }
}

