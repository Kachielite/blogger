package com.derrick.blogger.utils;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.springframework.stereotype.Component;

@Component
public class TokenGenerator {
    private static final int TOKEN_LENGTH = 64;

    public static String generateUniqueToken() {
        SecureRandom random = new SecureRandom();
        byte[] tokenBytes =
                new byte[TOKEN_LENGTH / 2]; // Because each byte is represented by 2 characters in hexadecimal format
        random.nextBytes(tokenBytes);
        return new BigInteger(1, tokenBytes).toString(16);
    }
}
