package com.tyonex.top_up.service;

import com.tyonex.top_up.dto.request.CompanyRequest;
import com.tyonex.top_up.dto.response.CompanyResponse;
import com.tyonex.top_up.entity.Company;
import com.tyonex.top_up.entity.CreditWallet;
import com.tyonex.top_up.exception.ResourceNotFoundException;
import com.tyonex.top_up.mapper.EntityMapper;
import com.tyonex.top_up.repository.CompanyRepository;
import com.tyonex.top_up.repository.CreditWalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CreditWalletRepository creditWalletRepository;
    private final EntityMapper mapper;

    @Transactional
    public CompanyResponse createCompany(Long userId, CompanyRequest request) {
        if (companyRepository.existsByTaxNo(request.getTaxNo())) {
            throw new RuntimeException("Tax number already exists");
        }

        Company company = mapper.toCompany(request);
        company.setUserId(userId);
        company = companyRepository.save(company);

        // Create wallet for company
        CreditWallet wallet = CreditWallet.builder()
                .companyId(company.getId())
                .creditBalance(BigDecimal.ZERO)
                .build();
        creditWalletRepository.save(wallet);

        return mapper.toCompanyResponse(company);
    }

    public CompanyResponse getMyCompany(Long userId) {
        Company company = companyRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found for user"));
        return mapper.toCompanyResponse(company);
    }

    public Company getCompanyById(Long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
    }

    public Company getCompanyByUserId(Long userId) {
        return companyRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found for user"));
    }

    @Transactional
    public CompanyResponse updateCompany(Long userId, CompanyRequest request) {
        Company company = companyRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found for user"));

        // Check if tax number is being changed and if it already exists
        if (!company.getTaxNo().equals(request.getTaxNo()) && 
            companyRepository.existsByTaxNo(request.getTaxNo())) {
            throw new RuntimeException("Tax number already exists");
        }

        // Update company fields
        company.setTitle(request.getTitle());
        company.setTaxNo(request.getTaxNo());
        if (request.getTaxOffice() != null) company.setTaxOffice(request.getTaxOffice());
        if (request.getAddress() != null) company.setAddress(request.getAddress());
        if (request.getPhone() != null) company.setPhone(request.getPhone());
        if (request.getEmail() != null) company.setEmail(request.getEmail());
        if (request.getGibSenderAccount() != null) company.setGibSenderAccount(request.getGibSenderAccount());
        if (request.getGibReceiverAccount() != null) company.setGibReceiverAccount(request.getGibReceiverAccount());

        company = companyRepository.save(company);
        return mapper.toCompanyResponse(company);
    }
}

