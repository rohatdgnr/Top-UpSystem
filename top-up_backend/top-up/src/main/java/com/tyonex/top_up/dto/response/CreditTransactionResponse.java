package com.tyonex.top_up.dto.response;

import com.tyonex.top_up.entity.CreditTransaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditTransactionResponse {
    private Long id;
    private Long companyId;
    private CreditTransaction.TransactionType type;
    private BigDecimal amount;
    private String reason;
    private Long relatedDocumentId;
    private LocalDateTime createdAt;
}

