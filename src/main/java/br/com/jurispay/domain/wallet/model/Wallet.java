package br.com.jurispay.domain.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {

    private Long id;
    private String ownerType;
    private Long ownerId;
    private BigDecimal saldo;
    private Instant dataCriacao;
    private Instant dataAtualizacao;
}
