package com.joaze.estoqueapi.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "Email must not be blank")
        @Email(message = "E-mail invalid")
        String email,
        @NotBlank(message = "The password must not be blank")
        @Size(min = 6, message = "Password must have at least 6 characters")
        String password
) {}
