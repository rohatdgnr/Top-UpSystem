package com.tyonex.top_up.dto.request;

import com.tyonex.top_up.entity.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DocumentRequest {
    @NotNull(message = "Document type is required")
    private Document.DocumentType documentType;

    @NotBlank(message = "JSON payload is required")
    private String jsonPayload;
}

