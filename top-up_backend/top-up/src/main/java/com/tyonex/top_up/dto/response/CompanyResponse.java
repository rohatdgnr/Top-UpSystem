package com.tyonex.top_up.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponse {
    private Long id;
    private Long userId;
    private String title;
    private String taxNo;
    private String taxOffice;
    private String address;
    private String phone;
    private String email;
    private String gibSenderAccount;
    private String gibReceiverAccount;
    private LocalDateTime createdAt;
}

