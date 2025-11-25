package com.tyonex.top_up.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreditPackageRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @NotNull(message = "Credit amount is required")
    @Positive(message = "Credit amount must be positive")
    private Integer creditAmount;

    private Boolean isActive = true;
}

