package com.derrick.blogger.dto;

import com.derrick.blogger.enums.BlogStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record BlogRequestDTO(
        @NotNull(message = "title cannot be blank") String title,
        @NotNull(message = "coverPhoto cannot be blank") MultipartFile coverPhoto,
        @NotNull(message = "content cannot be blank") String content,
        @NotNull(message = "content cannot be blank") String tags,
        @NotNull(message = "content cannot be blank") BlogStatus status) {}

