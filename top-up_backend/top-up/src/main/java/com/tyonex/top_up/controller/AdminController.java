package com.tyonex.top_up.controller;

import com.tyonex.top_up.dto.response.ApiResponse;
import com.tyonex.top_up.dto.response.CompanyResponse;
import com.tyonex.top_up.dto.response.CreditTransactionResponse;
import com.tyonex.top_up.dto.response.DocumentResponse;
import com.tyonex.top_up.entity.User;
import com.tyonex.top_up.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = adminService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.<List<User>>builder()
                .success(true)
                .data(users)
                .build());
    }

    @GetMapping("/companies")
    public ResponseEntity<ApiResponse<List<CompanyResponse>>> getAllCompanies() {
        List<CompanyResponse> companies = adminService.getAllCompanies();
        return ResponseEntity.ok(ApiResponse.<List<CompanyResponse>>builder()
                .success(true)
                .data(companies)
                .build());
    }

    @GetMapping("/transactions")
    public ResponseEntity<ApiResponse<List<CreditTransactionResponse>>> getAllTransactions() {
        List<CreditTransactionResponse> transactions = adminService.getAllTransactions();
        return ResponseEntity.ok(ApiResponse.<List<CreditTransactionResponse>>builder()
                .success(true)
                .data(transactions)
                .build());
    }

    @GetMapping("/documents")
    public ResponseEntity<ApiResponse<List<DocumentResponse>>> getAllDocuments() {
        List<DocumentResponse> documents = adminService.getAllDocuments();
        return ResponseEntity.ok(ApiResponse.<List<DocumentResponse>>builder()
                .success(true)
                .data(documents)
                .build());
    }
}

