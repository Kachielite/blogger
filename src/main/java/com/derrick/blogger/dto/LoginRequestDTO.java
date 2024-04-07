package com.derrick.blogger.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(@NotBlank String email, @NotBlank String password) {}
