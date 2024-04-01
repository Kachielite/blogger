package com.derrick.blogger.dto;

import com.derrick.blogger.model.Role;

import java.util.Set;

public record UserRequestDTO(
        String password,
        String profilePhoto,
        String bio,
        String x,
        String instagram,
        String facebook,
        Set<Role> authorities
) { }
