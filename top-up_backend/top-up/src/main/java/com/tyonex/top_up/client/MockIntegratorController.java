package com.tyonex.top_up.client;

import com.tyonex.top_up.dto.response.IntegratorResponse;
import com.tyonex.top_up.dto.response.IntegratorStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Mock integrator endpoint for testing
 * This simulates the external integrator API
 */
@RestController
@RequestMapping("/mock")
public class MockIntegratorController {

    @PostMapping("/sendDocument")
    public IntegratorResponse sendDocument(@RequestBody IntegratorClient.DocumentPayload payload) {
        // Simulate processing delay
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Generate mock tracking ID
        String trackingId = "TRACK_" + System.currentTimeMillis();

        return IntegratorResponse.builder()
                .trackingId(trackingId)
                .result("SUCCESS")
                .description("Document received and processed successfully")
                .build();
    }

    @GetMapping("/status/{trackingId}")
    public IntegratorStatus getStatus(@PathVariable String trackingId) {
        return IntegratorStatus.builder()
                .trackingId(trackingId)
                .status("SENT")
                .description("Document status retrieved")
                .build();
    }
}

