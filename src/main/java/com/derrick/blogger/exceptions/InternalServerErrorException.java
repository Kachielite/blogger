package com.derrick.blogger.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Something went wrong")
public class InternalServerErrorException extends Exception{
    public InternalServerErrorException(String message){super(message);}
}
