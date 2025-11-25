package com.tyonex.top_up.controller;

import com.tyonex.top_up.dto.request.CreditLoadRequest;
import com.tyonex.top_up.dto.response.ApiResponse;
import com.tyonex.top_up.dto.response.WalletResponse;
import com.tyonex.top_up.service.CompanyService;
import com.tyonex.top_up.service.WalletService;
import com.tyonex.top_up.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;
    private final CompanyService companyService;
    private final SecurityUtil securityUtil;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<WalletResponse>> getMyWallet(Authentication authentication) {
        Long userId = securityUtil.getUserIdFromAuthentication(authentication);
        Long companyId = companyService.getCompanyByUserId(userId).getId();
        WalletResponse response = walletService.getMyWallet(companyId);
        return ResponseEntity.ok(ApiResponse.<WalletResponse>builder()
                .success(true)
                .data(response)
                .build());
    }

    @PostMapping("/load")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<WalletResponse>> loadCredit(
            @Valid @RequestBody CreditLoadRequest request) {
        WalletResponse response = walletService.loadCredit(request);
        return ResponseEntity.ok(ApiResponse.<WalletResponse>builder()
                .success(true)
                .message("Credit loaded successfully")
                .data(response)
                .build());
    }
}

