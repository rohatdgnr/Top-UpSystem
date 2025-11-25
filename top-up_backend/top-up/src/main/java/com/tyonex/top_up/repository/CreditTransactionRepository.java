package com.tyonex.top_up.repository;

import com.tyonex.top_up.entity.CreditTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditTransactionRepository extends JpaRepository<CreditTransaction, Long> {
    List<CreditTransaction> findByCompanyIdOrderByCreatedAtDesc(Long companyId);
}

