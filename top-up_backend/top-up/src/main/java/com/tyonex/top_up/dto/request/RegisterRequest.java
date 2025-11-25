package com.tyonex.top_up.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    // Company information
    @NotBlank(message = "Tax number is required")
    @Size(min = 10, max = 11, message = "Tax number must be 10 or 11 characters")
    private String taxNo;

    @NotBlank(message = "Company title is required")
    @Size(min = 2, max = 200, message = "Company title must be between 2 and 200 characters")
    private String companyTitle;
}

