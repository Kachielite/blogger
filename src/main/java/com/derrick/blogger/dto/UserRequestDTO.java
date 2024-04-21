package com.derrick.blogger.dto;

import org.springframework.web.multipart.MultipartFile;

public record UserRequestDTO(
        String firstName,
        String lastName,
        MultipartFile profilePhoto,
        String bio,
        String x,
        String instagram,
        String facebook,
        String password,
        String token) {}
