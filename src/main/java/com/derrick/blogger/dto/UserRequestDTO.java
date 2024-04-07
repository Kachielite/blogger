package com.derrick.blogger.dto;

public record UserRequestDTO(
        String password, String profilePhoto, String bio, String x, String instagram, String facebook, String role) {}
