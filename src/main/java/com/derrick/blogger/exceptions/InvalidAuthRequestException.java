package com.derrick.blogger.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
        code = HttpStatus.UNAUTHORIZED,
        reason = "Invalid authentication request. Please check your credentials and try again.")
public class InvalidAuthRequestException extends Exception {
    public InvalidAuthRequestException(String message) {
        super(message);
    }
}
