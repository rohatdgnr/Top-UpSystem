package com.tyonex.top_up.dto.response;

import com.tyonex.top_up.entity.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponse {
    private Long id;
    private Long companyId;
    private Document.DocumentType documentType;
    private Document.DocumentStatus status;
    private String jsonPayload;
    private String entegratorTrackingId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

