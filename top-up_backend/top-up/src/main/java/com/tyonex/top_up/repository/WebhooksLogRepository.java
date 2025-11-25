package com.tyonex.top_up.repository;

import com.tyonex.top_up.entity.WebhooksLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebhooksLogRepository extends JpaRepository<WebhooksLog, Long> {
    List<WebhooksLog> findByProcessedFalse();
}

