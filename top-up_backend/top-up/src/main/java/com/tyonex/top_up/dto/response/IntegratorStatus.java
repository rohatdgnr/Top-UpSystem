package com.tyonex.top_up.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntegratorStatus {
    private String trackingId;
    private String status;
    private String description;
}

