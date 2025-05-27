package org.example.exception;

public class DuplicateReactionException extends RuntimeException {
    public DuplicateReactionException(String message) {
        super(message);
    }
}
