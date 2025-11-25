package com.tyonex.top_up.controller;

import com.tyonex.top_up.dto.response.ApiResponse;
import com.tyonex.top_up.service.WebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhook")
@RequiredArgsConstructor
public class WebhookController {
    private final WebhookService webhookService;

    @PostMapping("/entegrator")
    public ResponseEntity<ApiResponse<Object>> receiveWebhook(
            @RequestParam(required = false, defaultValue = "entegrator") String source,
            @RequestBody String payload) {
        webhookService.processWebhook(source, payload);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Webhook received and processed")
                .build());
    }
}

