package com.derrick.blogger.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    String uploadImage(MultipartFile image, String folderName) throws IOException;

    void deleteImage(String imageUrl) throws IOException;
}
