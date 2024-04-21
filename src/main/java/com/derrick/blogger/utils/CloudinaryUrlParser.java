package com.derrick.blogger.utils;

import org.springframework.stereotype.Component;

@Component
public class CloudinaryUrlParser {
    public String extractPublicId(String url) {
        // Split the URL by "/"
        String[] parts = url.split("/");

        // The public ID is typically the last part of the URL
        return parts[parts.length - 1];
    }
}
