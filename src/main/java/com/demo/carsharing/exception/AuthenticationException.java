package com.demo.carsharing.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message, Object... formatArgs) {
        super(message.formatted(formatArgs));
    }
}
