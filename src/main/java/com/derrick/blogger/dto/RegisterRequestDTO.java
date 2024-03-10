package com.derrick.blogger.dto;

import com.derrick.blogger.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record RegisterRequestDTO(
        @NotBlank String username, @NotBlank String password, @NotNull Set<Role> authorities) {}
