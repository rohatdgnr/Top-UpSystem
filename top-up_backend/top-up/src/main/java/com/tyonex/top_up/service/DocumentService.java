package com.tyonex.top_up.service;

import com.tyonex.top_up.client.IntegratorClient;
import com.tyonex.top_up.dto.request.DocumentRequest;
import com.tyonex.top_up.dto.response.DocumentResponse;
import com.tyonex.top_up.dto.response.IntegratorResponse;
import com.tyonex.top_up.entity.Company;
import com.tyonex.top_up.entity.Document;
import com.tyonex.top_up.exception.DocumentAlreadyProcessedException;
import com.tyonex.top_up.exception.InsufficientCreditException;
import com.tyonex.top_up.exception.ResourceNotFoundException;
import com.tyonex.top_up.mapper.EntityMapper;
import com.tyonex.top_up.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final CompanyService companyService;
    private final WalletService walletService;
    private final CreditCostService creditCostService;
    private final IntegratorClient integratorClient;
    private final EntityMapper mapper;

    @Transactional
    public DocumentResponse createDocument(Long userId, DocumentRequest request) {
        Company company = companyService.getCompanyByUserId(userId);
        
        Document document = Document.builder()
                .companyId(company.getId())
                .documentType(request.getDocumentType())
                .status(Document.DocumentStatus.PENDING)
                .jsonPayload(request.getJsonPayload())
                .build();

        document = documentRepository.save(document);
        return mapper.toDocumentResponse(document);
    }

    @Transactional
    public DocumentResponse sendDocument(Long userId, Long documentId) {
        Company company = companyService.getCompanyByUserId(userId);
        
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        if (!document.getCompanyId().equals(company.getId())) {
            throw new ResourceNotFoundException("Document does not belong to your company");
        }

        if (document.getStatus() != Document.DocumentStatus.PENDING) {
            throw new DocumentAlreadyProcessedException(
                    "Document is already processed. Current status: " + document.getStatus() + 
                    ". Only PENDING documents can be sent."
            );
        }

        BigDecimal requiredCredit = creditCostService.getCreditCost(document.getDocumentType());

        if (!walletService.hasSufficientCredit(company.getId(), requiredCredit)) {
            document.setStatus(Document.DocumentStatus.FAILED);
            documentRepository.save(document);
            throw new InsufficientCreditException("Insufficient credit balance");
        }

        try {
            // Send to integrator
            IntegratorClient.DocumentPayload payload = new IntegratorClient.DocumentPayload(
                    document.getDocumentType().name(),
                    document.getJsonPayload(),
                    company.getTaxNo()
            );

            IntegratorResponse response = integratorClient.sendDocument(payload);

            if ("SUCCESS".equals(response.getResult())) {
                // Deduct credit
                walletService.deductCredit(
                        company.getId(),
                        requiredCredit,
                        "Document sent: " + document.getDocumentType(),
                        document.getId()
                );

                document.setStatus(Document.DocumentStatus.SENT);
                document.setEntegratorTrackingId(response.getTrackingId());
            } else {
                document.setStatus(Document.DocumentStatus.FAILED);
            }
        } catch (feign.RetryableException e) {
            log.error("Failed to connect to integrator service: {}", e.getMessage());
            document.setStatus(Document.DocumentStatus.FAILED);
        } catch (feign.FeignException.Forbidden e) {
            log.error("Access forbidden when calling integrator service: {}", e.getMessage());
            document.setStatus(Document.DocumentStatus.FAILED);
        } catch (feign.FeignException e) {
            log.error("Feign error when calling integrator service: {} - Status: {}", e.getMessage(), e.status());
            document.setStatus(Document.DocumentStatus.FAILED);
        } catch (Exception e) {
            log.error("Error sending document to integrator: {}", e.getMessage(), e);
            document.setStatus(Document.DocumentStatus.FAILED);
        }

        document = documentRepository.save(document);
        return mapper.toDocumentResponse(document);
    }

    public List<DocumentResponse> getMyDocuments(Long userId) {
        Company company = companyService.getCompanyByUserId(userId);
        return documentRepository.findByCompanyIdOrderByCreatedAtDesc(company.getId())
                .stream()
                .map(mapper::toDocumentResponse)
                .collect(Collectors.toList());
    }

    public DocumentResponse getDocumentById(Long userId, Long documentId) {
        Company company = companyService.getCompanyByUserId(userId);
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        if (!document.getCompanyId().equals(company.getId())) {
            throw new ResourceNotFoundException("Document does not belong to your company");
        }

        return mapper.toDocumentResponse(document);
    }

    public List<DocumentResponse> getAllDocuments() {
        return documentRepository.findAll().stream()
                .map(mapper::toDocumentResponse)
                .collect(Collectors.toList());
    }
}

