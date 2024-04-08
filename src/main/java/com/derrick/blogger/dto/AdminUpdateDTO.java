package com.derrick.blogger.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdminUpdateDTO(
        @NotNull(message = "User ID cannot be blank") Integer userId,
        @NotBlank(message = "Role cannot be blank") @NotNull(message = "Role cannot be blank") String role) {}
