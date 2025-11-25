package com.tyonex.top_up.exception;

public class InsufficientCreditException extends RuntimeException {
    public InsufficientCreditException(String message) {
        super(message);
    }
}

