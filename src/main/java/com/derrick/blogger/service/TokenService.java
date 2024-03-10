package com.derrick.blogger.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public interface TokenService {
    String generateJwt(Authentication authentication);
}
