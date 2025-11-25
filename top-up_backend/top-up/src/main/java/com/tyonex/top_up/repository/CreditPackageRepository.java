package com.tyonex.top_up.repository;

import com.tyonex.top_up.entity.CreditPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditPackageRepository extends JpaRepository<CreditPackage, Long> {
    List<CreditPackage> findByIsActiveTrue();
}

