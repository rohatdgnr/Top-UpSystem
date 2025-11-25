package com.tyonex.top_up.repository;

import com.tyonex.top_up.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByCompanyIdOrderByCreatedAtDesc(Long companyId);
    Optional<Document> findByEntegratorTrackingId(String trackingId);
    List<Document> findByStatus(Document.DocumentStatus status);
}

