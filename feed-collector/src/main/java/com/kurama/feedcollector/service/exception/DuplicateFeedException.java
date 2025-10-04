package com.kurama.feedcollector.service.exception;

public class DuplicateFeedException extends RuntimeException {
    public DuplicateFeedException(String message) {
        super(message);
    }

    public DuplicateFeedException(String message, Throwable cause) {
        super(message, cause);
    }
}
