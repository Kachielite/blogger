package com.derrick.blogger.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "A conflict occured")
public class ConflictException extends Exception{
    public ConflictException(String message) {
        super(message);
    }
}
