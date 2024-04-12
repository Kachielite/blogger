package com.derrick.blogger.dto;

import org.springframework.web.multipart.MultipartFile;

public record UserRequestDTO(MultipartFile profilePhoto, String bio, String x, String instagram, String facebook) {}
