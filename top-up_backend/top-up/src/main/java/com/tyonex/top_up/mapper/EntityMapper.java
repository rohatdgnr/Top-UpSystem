package com.tyonex.top_up.mapper;

import com.tyonex.top_up.dto.request.CompanyRequest;
import com.tyonex.top_up.dto.request.CreditPackageRequest;
import com.tyonex.top_up.dto.response.CompanyResponse;
import com.tyonex.top_up.dto.response.CreditPackageResponse;
import com.tyonex.top_up.dto.response.CreditTransactionResponse;
import com.tyonex.top_up.dto.response.DocumentResponse;
import com.tyonex.top_up.dto.response.WalletResponse;
import com.tyonex.top_up.entity.Company;
import com.tyonex.top_up.entity.CreditPackage;
import com.tyonex.top_up.entity.CreditTransaction;
import com.tyonex.top_up.entity.CreditWallet;
import com.tyonex.top_up.entity.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EntityMapper {
    CompanyResponse toCompanyResponse(Company company);
    Company toCompany(CompanyRequest request);
    
    WalletResponse toWalletResponse(CreditWallet wallet);
    
    CreditPackageResponse toCreditPackageResponse(CreditPackage creditPackage);
    CreditPackage toCreditPackage(CreditPackageRequest request);
    
    DocumentResponse toDocumentResponse(Document document);
    
    CreditTransactionResponse toCreditTransactionResponse(CreditTransaction transaction);
}

