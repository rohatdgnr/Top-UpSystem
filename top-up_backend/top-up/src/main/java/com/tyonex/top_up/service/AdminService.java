package com.tyonex.top_up.service;

import com.tyonex.top_up.dto.response.CompanyResponse;
import com.tyonex.top_up.dto.response.CreditTransactionResponse;
import com.tyonex.top_up.dto.response.DocumentResponse;
import com.tyonex.top_up.entity.User;
import com.tyonex.top_up.mapper.EntityMapper;
import com.tyonex.top_up.repository.CompanyRepository;
import com.tyonex.top_up.repository.CreditTransactionRepository;
import com.tyonex.top_up.repository.DocumentRepository;
import com.tyonex.top_up.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final CreditTransactionRepository transactionRepository;
    private final DocumentRepository documentRepository;
    private final EntityMapper mapper;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<CompanyResponse> getAllCompanies() {
        return companyRepository.findAll().stream()
                .map(mapper::toCompanyResponse)
                .collect(Collectors.toList());
    }

    public List<CreditTransactionResponse> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(mapper::toCreditTransactionResponse)
                .collect(Collectors.toList());
    }

    public List<DocumentResponse> getAllDocuments() {
        return documentRepository.findAll().stream()
                .map(mapper::toDocumentResponse)
                .collect(Collectors.toList());
    }
}

