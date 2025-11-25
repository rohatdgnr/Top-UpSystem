package com.tyonex.top_up.controller;

import com.tyonex.top_up.dto.request.CompanyRequest;
import com.tyonex.top_up.dto.response.ApiResponse;
import com.tyonex.top_up.dto.response.CompanyResponse;
import com.tyonex.top_up.service.CompanyService;
import com.tyonex.top_up.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    private final SecurityUtil securityUtil;

    @PostMapping
    public ResponseEntity<ApiResponse<CompanyResponse>> createCompany(
            @Valid @RequestBody CompanyRequest request,
            Authentication authentication) {
        Long userId = securityUtil.getUserIdFromAuthentication(authentication);
        CompanyResponse response = companyService.createCompany(userId, request);
        return ResponseEntity.ok(ApiResponse.<CompanyResponse>builder()
                .success(true)
                .message("Company created successfully")
                .data(response)
                .build());
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<CompanyResponse>> getMyCompany(Authentication authentication) {
        Long userId = securityUtil.getUserIdFromAuthentication(authentication);
        CompanyResponse response = companyService.getMyCompany(userId);
        return ResponseEntity.ok(ApiResponse.<CompanyResponse>builder()
                .success(true)
                .data(response)
                .build());
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<CompanyResponse>> updateMyCompany(
            @Valid @RequestBody CompanyRequest request,
            Authentication authentication) {
        Long userId = securityUtil.getUserIdFromAuthentication(authentication);
        CompanyResponse response = companyService.updateCompany(userId, request);
        return ResponseEntity.ok(ApiResponse.<CompanyResponse>builder()
                .success(true)
                .message("Company updated successfully")
                .data(response)
                .build());
    }
}

