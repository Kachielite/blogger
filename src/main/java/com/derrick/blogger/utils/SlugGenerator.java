package com.derrick.blogger.utils;

import org.springframework.stereotype.Component;

@Component
public class SlugGenerator {
    public String generateSlug(String title) {
        return title.toLowerCase().replaceAll("\\s+", "-");
    }
}
