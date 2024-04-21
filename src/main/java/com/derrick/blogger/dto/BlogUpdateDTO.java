package com.derrick.blogger.dto;

import com.derrick.blogger.enums.BlogStatus;
import org.springframework.web.multipart.MultipartFile;

public record BlogUpdateDTO(String title, MultipartFile coverPhoto, String content, String tags, BlogStatus status) {}
