package com.tyonex.top_up.client;

import com.tyonex.top_up.dto.response.IntegratorResponse;
import com.tyonex.top_up.dto.response.IntegratorStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "IntegratorClient", url = "${integrator.api.url}")
public interface IntegratorClient {
    @PostMapping("/sendDocument")
    IntegratorResponse sendDocument(@RequestBody DocumentPayload payload);

    @GetMapping("/status/{trackingId}")
    IntegratorStatus getStatus(@PathVariable String trackingId);

    record DocumentPayload(String documentType, String jsonPayload, String companyTaxNo) {}
}

