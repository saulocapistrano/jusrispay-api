package br.com.jurispay.infrastructure.persistence.mapper;

import br.com.jurispay.domain.wallet.model.Wallet;
import br.com.jurispay.infrastructure.persistence.jpa.entity.WalletEntity;
import org.springframework.stereotype.Component;

@Component
public class WalletEntityMapper {

    public Wallet toDomain(WalletEntity entity) {
        if (entity == null) {
            return null;
        }
        return Wallet.builder()
                .id(entity.getId())
                .ownerType(entity.getOwnerType())
                .ownerId(entity.getOwnerId())
                .saldo(entity.getSaldo())
                .dataCriacao(entity.getDataCriacao())
                .dataAtualizacao(entity.getDataAtualizacao())
                .build();
    }

    public WalletEntity toEntity(Wallet wallet) {
        if (wallet == null) {
            return null;
        }
        return WalletEntity.builder()
                .id(wallet.getId())
                .ownerType(wallet.getOwnerType())
                .ownerId(wallet.getOwnerId())
                .saldo(wallet.getSaldo())
                .dataCriacao(wallet.getDataCriacao())
                .dataAtualizacao(wallet.getDataAtualizacao())
                .build();
    }
}
