package com.tyonex.top_up.repository;

import com.tyonex.top_up.entity.CreditWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface CreditWalletRepository extends JpaRepository<CreditWallet, Long> {
    Optional<CreditWallet> findByCompanyId(Long companyId);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<CreditWallet> findWithLockByCompanyId(Long companyId);
}

