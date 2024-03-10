package com.derrick.blogger.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Bad request")
public class BadRequestException extends Exception {
    public BadRequestException(String message) {
        super(message);
    }
}
