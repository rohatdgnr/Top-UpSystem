package com.tyonex.top_up.controller;

import com.tyonex.top_up.dto.request.DocumentRequest;
import com.tyonex.top_up.dto.response.ApiResponse;
import com.tyonex.top_up.dto.response.DocumentResponse;
import com.tyonex.top_up.entity.Document;
import com.tyonex.top_up.service.DocumentService;
import com.tyonex.top_up.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;
    private final SecurityUtil securityUtil;

    @PostMapping
    public ResponseEntity<ApiResponse<DocumentResponse>> createDocument(
            @Valid @RequestBody DocumentRequest request,
            Authentication authentication) {
        Long userId = securityUtil.getUserIdFromAuthentication(authentication);
        DocumentResponse response = documentService.createDocument(userId, request);
        return ResponseEntity.ok(ApiResponse.<DocumentResponse>builder()
                .success(true)
                .message("Document created successfully")
                .data(response)
                .build());
    }

    @PostMapping("/{id}/send")
    public ResponseEntity<ApiResponse<DocumentResponse>> sendDocument(
            @PathVariable Long id,
            Authentication authentication) {
        Long userId = securityUtil.getUserIdFromAuthentication(authentication);
        DocumentResponse response = documentService.sendDocument(userId, id);
        
        // Determine message based on document status
        String message;
        boolean success;
        if (response.getStatus() == Document.DocumentStatus.SENT) {
            message = "Document sent successfully";
            success = true;
        } else if (response.getStatus() == Document.DocumentStatus.FAILED) {
            message = "Failed to send document. Please check your credit balance and try again.";
            success = false;
        } else {
            message = "Document processing status: " + response.getStatus();
            success = false;
        }
        
        return ResponseEntity.status(success ? HttpStatus.OK : HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<DocumentResponse>builder()
                        .success(success)
                        .message(message)
                        .data(response)
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DocumentResponse>>> getMyDocuments(Authentication authentication) {
        Long userId = securityUtil.getUserIdFromAuthentication(authentication);
        List<DocumentResponse> documents = documentService.getMyDocuments(userId);
        return ResponseEntity.ok(ApiResponse.<List<DocumentResponse>>builder()
                .success(true)
                .data(documents)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DocumentResponse>> getDocumentById(
            @PathVariable Long id,
            Authentication authentication) {
        Long userId = securityUtil.getUserIdFromAuthentication(authentication);
        DocumentResponse response = documentService.getDocumentById(userId, id);
        return ResponseEntity.ok(ApiResponse.<DocumentResponse>builder()
                .success(true)
                .data(response)
                .build());
    }
}

