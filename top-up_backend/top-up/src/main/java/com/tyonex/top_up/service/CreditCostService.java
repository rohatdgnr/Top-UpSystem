package com.tyonex.top_up.service;

import com.tyonex.top_up.entity.Document;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class CreditCostService {
    private static final Map<Document.DocumentType, BigDecimal> CREDIT_COST_MAP = new HashMap<>();

    static {
        CREDIT_COST_MAP.put(Document.DocumentType.EFATURA, BigDecimal.ONE);
        CREDIT_COST_MAP.put(Document.DocumentType.EARSIV, BigDecimal.ONE);
    }

    public BigDecimal getCreditCost(Document.DocumentType documentType) {
        return CREDIT_COST_MAP.getOrDefault(documentType, BigDecimal.ONE);
    }
}

