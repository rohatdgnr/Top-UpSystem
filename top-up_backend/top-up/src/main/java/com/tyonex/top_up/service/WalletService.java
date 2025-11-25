package com.tyonex.top_up.service;

import com.tyonex.top_up.dto.request.CreditLoadRequest;
import com.tyonex.top_up.dto.response.CreditTransactionResponse;
import com.tyonex.top_up.dto.response.WalletResponse;
import com.tyonex.top_up.entity.CreditTransaction;
import com.tyonex.top_up.entity.CreditWallet;
import com.tyonex.top_up.exception.InsufficientCreditException;
import com.tyonex.top_up.exception.ResourceNotFoundException;
import com.tyonex.top_up.mapper.EntityMapper;
import com.tyonex.top_up.repository.CreditPackageRepository;
import com.tyonex.top_up.repository.CreditTransactionRepository;
import com.tyonex.top_up.repository.CreditWalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final CreditWalletRepository walletRepository;
    private final CreditTransactionRepository transactionRepository;
    private final CreditPackageRepository packageRepository;
    private final CompanyService companyService;
    private final EntityMapper mapper;

    public WalletResponse getMyWallet(Long companyId) {
        CreditWallet wallet = walletRepository.findByCompanyId(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));
        return mapper.toWalletResponse(wallet);
    }

    @Transactional
    public WalletResponse loadCredit(CreditLoadRequest request) {
        // Check if company exists first
        try {
            companyService.getCompanyById(request.getCompanyId());
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Company not found with ID: " + request.getCompanyId() + ". Please check the company ID.");
        }
        
        // Get or create wallet
        CreditWallet wallet = walletRepository.findWithLockByCompanyId(request.getCompanyId())
                .orElseGet(() -> {
                    // Create wallet if it doesn't exist
                    CreditWallet newWallet = CreditWallet.builder()
                            .companyId(request.getCompanyId())
                            .creditBalance(BigDecimal.ZERO)
                            .build();
                    return walletRepository.save(newWallet);
                });

        BigDecimal creditAmount;
        String reason;

        if (request.getPackageId() != null) {
            // Load from package
            var creditPackage = packageRepository.findById(request.getPackageId())
                    .orElseThrow(() -> new ResourceNotFoundException("Credit package not found"));

            if (!creditPackage.getIsActive()) {
                throw new RuntimeException("Credit package is not active");
            }

            creditAmount = BigDecimal.valueOf(creditPackage.getCreditAmount());
            reason = "Credit loaded from package: " + creditPackage.getName();
        } else {
            // Direct credit loading (admin manual load)
            creditAmount = request.getAmount();
            reason = request.getDescription() != null && !request.getDescription().isEmpty()
                    ? request.getDescription()
                    : "Credit loaded manually by admin";
        }

        wallet.setCreditBalance(wallet.getCreditBalance().add(creditAmount));
        wallet = walletRepository.save(wallet);

        // Create transaction
        CreditTransaction transaction = CreditTransaction.builder()
                .companyId(request.getCompanyId())
                .type(CreditTransaction.TransactionType.LOAD)
                .amount(creditAmount)
                .reason(reason)
                .build();
        transactionRepository.save(transaction);

        return mapper.toWalletResponse(wallet);
    }

    @Transactional
    public void deductCredit(Long companyId, BigDecimal amount, String reason, Long documentId) {
        CreditWallet wallet = walletRepository.findWithLockByCompanyId(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

        if (wallet.getCreditBalance().compareTo(amount) < 0) {
            throw new InsufficientCreditException("Insufficient credit balance");
        }

        wallet.setCreditBalance(wallet.getCreditBalance().subtract(amount));
        walletRepository.save(wallet);

        // Create transaction
        CreditTransaction transaction = CreditTransaction.builder()
                .companyId(companyId)
                .type(CreditTransaction.TransactionType.SPEND)
                .amount(amount)
                .reason(reason)
                .relatedDocumentId(documentId)
                .build();
        transactionRepository.save(transaction);
    }

    public boolean hasSufficientCredit(Long companyId, BigDecimal requiredAmount) {
        return walletRepository.findByCompanyId(companyId)
                .map(wallet -> wallet.getCreditBalance().compareTo(requiredAmount) >= 0)
                .orElse(false);
    }
}

