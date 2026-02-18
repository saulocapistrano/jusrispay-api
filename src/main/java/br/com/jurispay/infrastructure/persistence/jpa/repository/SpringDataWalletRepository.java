package br.com.jurispay.infrastructure.persistence.jpa.repository;

import br.com.jurispay.infrastructure.persistence.jpa.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

import java.util.Optional;

public interface SpringDataWalletRepository extends JpaRepository<WalletEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select w from WalletEntity w where w.ownerType = :ownerType and w.ownerId = :ownerId")
    Optional<WalletEntity> findForUpdateByOwner(@Param("ownerType") String ownerType, @Param("ownerId") Long ownerId);

    Optional<WalletEntity> findByOwnerTypeAndOwnerId(String ownerType, Long ownerId);
}
