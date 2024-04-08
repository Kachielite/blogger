package com.derrick.blogger.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdminRequestDTO(
        @NotBlank(message = "Email cannot be blank")
                @NotNull(message = "Email cannot be blank")
                @Email(message = "Invalid email")
                String email,
        @NotBlank(message = "Password cannot be blank") @NotNull(message = "Password cannot be blank") String password,
        @NotBlank(message = "Role cannot be blank") @NotNull(message = "Role cannot be blank") String role) {}
