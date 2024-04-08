package com.derrick.blogger.exceptions;

public class InsufficientPermissionsException extends Exception {
    public InsufficientPermissionsException(String message) {
        super(message);
    }
}
