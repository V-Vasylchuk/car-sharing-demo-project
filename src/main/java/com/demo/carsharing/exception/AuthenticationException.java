package com.demo.carsharing.exception;

public class AuthenticationException extends Exception {
    public AuthenticationException(String message, Object... formatArgs) {
        super(message.formatted(formatArgs));
    }
}
