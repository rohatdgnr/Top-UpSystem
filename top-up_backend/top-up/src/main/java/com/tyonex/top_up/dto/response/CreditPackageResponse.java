package com.tyonex.top_up.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditPackageResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer creditAmount;
    private Boolean isActive;
    private String description;
}

