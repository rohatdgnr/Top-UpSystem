package com.tyonex.top_up.exception;

public class DocumentAlreadyProcessedException extends RuntimeException {
    public DocumentAlreadyProcessedException(String message) {
        super(message);
    }
}

