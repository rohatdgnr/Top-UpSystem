package com.tyonex.top_up.service;

import com.tyonex.top_up.entity.Document;
import com.tyonex.top_up.entity.WebhooksLog;
import com.tyonex.top_up.repository.DocumentRepository;
import com.tyonex.top_up.repository.WebhooksLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebhookService {
    private final WebhooksLogRepository webhooksLogRepository;
    private final DocumentRepository documentRepository;

    @Transactional
    public void processWebhook(String source, String payload) {
        // Log webhook
        WebhooksLog webhookLog = WebhooksLog.builder()
                .source(source)
                .payload(payload)
                .processed(false)
                .build();
        webhookLog = webhooksLogRepository.save(webhookLog);

        try {
            // Parse payload and update document status
            // This is a simplified version - you should parse the actual webhook payload
            // For now, we'll just log it
            log.info("Processing webhook from {}: {}", source, payload);

            // Example: If webhook contains trackingId, update document
            // You should implement proper JSON parsing here
            webhookLog.setProcessed(true);
            webhooksLogRepository.save(webhookLog);
        } catch (Exception e) {
            log.error("Error processing webhook", e);
        }
    }

    @Transactional
    public void updateDocumentStatus(String trackingId, Document.DocumentStatus status) {
        documentRepository.findByEntegratorTrackingId(trackingId)
                .ifPresent(document -> {
                    document.setStatus(status);
                    documentRepository.save(document);
                });
    }
}

