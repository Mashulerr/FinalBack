package org.example.exception;

public class NotFoundRuntimeException extends RuntimeException {
    public NotFoundRuntimeException(String message) {
        super(message);
    }
}
