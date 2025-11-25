package com.tyonex.top_up.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CompanyRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Tax number is required")
    private String taxNo;

    private String taxOffice;
    private String address;
    private String phone;
    private String email;
    private String gibSenderAccount;
    private String gibReceiverAccount;
}

