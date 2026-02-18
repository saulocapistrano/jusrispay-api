package br.com.jurispay.domain.wallet.repository;

import br.com.jurispay.domain.wallet.model.Wallet;

import java.util.Optional;

public interface WalletRepository {

    Optional<Wallet> findByOwner(String ownerType, Long ownerId);

    Optional<Wallet> findForUpdateByOwner(String ownerType, Long ownerId);

    Wallet save(Wallet wallet);
}
