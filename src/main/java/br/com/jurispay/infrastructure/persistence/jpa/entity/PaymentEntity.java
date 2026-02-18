package br.com.jurispay.infrastructure.persistence.jpa.entity;

import br.com.jurispay.domain.payment.model.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Entidade JPA para Payment.
 * Representa a tabela payment no banco de dados.
 */
@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    private LoanEntity loan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fine_id")
    private FineEntity fine;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valorPago;

    @Column(nullable = false)
    private Instant dataPagamento;

    @Column(nullable = false)
    private int diasAtraso;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal multaTotal;

    @Column(name = "fine_times")
    private Integer fineTimes;

    @Column(name = "fine_unit_amount", precision = 19, scale = 2)
    private BigDecimal fineUnitAmount;

    @Column(name = "fine_total_amount", precision = 19, scale = 2)
    private BigDecimal fineTotalAmount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valorFinalRecebido;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal roiBrl;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal roiPercent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethod metodo;

    @Column(nullable = false, updatable = false)
    private Instant dataCriacao;

    @PrePersist
    protected void onCreate() {
        if (dataCriacao == null) {
            dataCriacao = Instant.now();
        }
    }
}

