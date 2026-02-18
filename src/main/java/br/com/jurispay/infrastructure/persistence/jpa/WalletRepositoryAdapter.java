package br.com.jurispay.infrastructure.persistence.jpa;

import br.com.jurispay.domain.wallet.model.Wallet;
import br.com.jurispay.domain.wallet.repository.WalletRepository;
import br.com.jurispay.infrastructure.persistence.jpa.entity.WalletEntity;
import br.com.jurispay.infrastructure.persistence.jpa.repository.SpringDataWalletRepository;
import br.com.jurispay.infrastructure.persistence.mapper.WalletEntityMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class WalletRepositoryAdapter implements WalletRepository {

    private final SpringDataWalletRepository springDataWalletRepository;
    private final WalletEntityMapper mapper;

    public WalletRepositoryAdapter(
            SpringDataWalletRepository springDataWalletRepository,
            WalletEntityMapper mapper) {
        this.springDataWalletRepository = springDataWalletRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Wallet> findByOwner(String ownerType, Long ownerId) {
        return springDataWalletRepository.findByOwnerTypeAndOwnerId(ownerType, ownerId)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Wallet> findForUpdateByOwner(String ownerType, Long ownerId) {
        return springDataWalletRepository.findForUpdateByOwner(ownerType, ownerId)
                .map(mapper::toDomain);
    }

    @Override
    public Wallet save(Wallet wallet) {
        WalletEntity entity = mapper.toEntity(wallet);
        WalletEntity saved = springDataWalletRepository.save(entity);
        return mapper.toDomain(saved);
    }
}
