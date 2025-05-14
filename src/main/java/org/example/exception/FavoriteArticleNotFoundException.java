package org.example.exception;

public class FavoriteArticleNotFoundException extends RuntimeException {
    public FavoriteArticleNotFoundException(String message) {
        super(message);
    }
}
