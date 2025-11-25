package com.tyonex.top_up.controller;

import com.tyonex.top_up.dto.request.CreditPackageRequest;
import com.tyonex.top_up.dto.response.ApiResponse;
import com.tyonex.top_up.dto.response.CreditPackageResponse;
import com.tyonex.top_up.service.CreditPackageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
@RequiredArgsConstructor
public class CreditPackageController {
    private final CreditPackageService packageService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CreditPackageResponse>>> getAllActivePackages() {
        List<CreditPackageResponse> packages = packageService.getAllActivePackages();
        return ResponseEntity.ok(ApiResponse.<List<CreditPackageResponse>>builder()
                .success(true)
                .data(packages)
                .build());
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<CreditPackageResponse>>> getAllPackages() {
        List<CreditPackageResponse> packages = packageService.getAllPackages();
        return ResponseEntity.ok(ApiResponse.<List<CreditPackageResponse>>builder()
                .success(true)
                .data(packages)
                .build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CreditPackageResponse>> createPackage(
            @Valid @RequestBody CreditPackageRequest request) {
        CreditPackageResponse response = packageService.createPackage(request);
        return ResponseEntity.ok(ApiResponse.<CreditPackageResponse>builder()
                .success(true)
                .message("Credit package created successfully")
                .data(response)
                .build());
    }
}

