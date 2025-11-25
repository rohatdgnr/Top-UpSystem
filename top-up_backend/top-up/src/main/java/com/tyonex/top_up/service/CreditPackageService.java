package com.tyonex.top_up.service;

import com.tyonex.top_up.dto.request.CreditPackageRequest;
import com.tyonex.top_up.dto.response.CreditPackageResponse;
import com.tyonex.top_up.entity.CreditPackage;
import com.tyonex.top_up.exception.ResourceNotFoundException;
import com.tyonex.top_up.mapper.EntityMapper;
import com.tyonex.top_up.repository.CreditPackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreditPackageService {
    private final CreditPackageRepository packageRepository;
    private final EntityMapper mapper;

    public List<CreditPackageResponse> getAllActivePackages() {
        return packageRepository.findByIsActiveTrue().stream()
                .map(mapper::toCreditPackageResponse)
                .collect(Collectors.toList());
    }

    public List<CreditPackageResponse> getAllPackages() {
        return packageRepository.findAll().stream()
                .map(mapper::toCreditPackageResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CreditPackageResponse createPackage(CreditPackageRequest request) {
        CreditPackage creditPackage = mapper.toCreditPackage(request);
        creditPackage = packageRepository.save(creditPackage);
        return mapper.toCreditPackageResponse(creditPackage);
    }

    public CreditPackageResponse getPackageById(Long id) {
        CreditPackage creditPackage = packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Credit package not found"));
        return mapper.toCreditPackageResponse(creditPackage);
    }
}

