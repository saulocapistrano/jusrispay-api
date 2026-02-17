package br.com.jurispay.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "wallet", uniqueConstraints = {
        @UniqueConstraint(name = "uk_wallet_owner", columnNames = {"owner_type", "owner_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner_type", nullable = false, length = 30)
    private String ownerType;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal saldo;

    @Column(nullable = false, updatable = false)
    private Instant dataCriacao;

    @Column(nullable = false)
    private Instant dataAtualizacao;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        if (dataCriacao == null) {
            dataCriacao = now;
        }
        if (dataAtualizacao == null) {
            dataAtualizacao = now;
        }
        if (saldo == null) {
            saldo = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = Instant.now();
        if (saldo == null) {
            saldo = BigDecimal.ZERO;
        }
    }
}
