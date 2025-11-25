package com.tyonex.top_up.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreditLoadRequest {
    @NotNull(message = "Company ID is required")
    private Long companyId;

    private Long packageId; // Optional: if provided, uses package credit amount

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    private String description; // Optional description for manual credit loading
}

