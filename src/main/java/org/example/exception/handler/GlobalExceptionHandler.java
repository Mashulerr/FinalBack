package org.example.exception.handler;

import org.example.exception.InvalidNewsException;
import org.example.exception.NewsNotFoundException;
import org.example.exception.UserAlreadyExistsException;
import org.example.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


    @ControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(UserNotFoundException.class)
        public ResponseEntity<String> handlePersonNotFoundException(UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(UserAlreadyExistsException.class)
        public ResponseEntity<String> handlePersonAlreadyExistsException(UserAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<String> handleGeneralException(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Произошла ошибка: " + e.getMessage());
        }

        @ExceptionHandler(NewsNotFoundException.class)
        public ResponseEntity<String> handleNewsNotFound(NewsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ошибка: " + e.getMessage());
        }

        @ExceptionHandler(InvalidNewsException.class)
        public ResponseEntity<String> handleInvalidNews(InvalidNewsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Ошибка: " + e.getMessage());
        }
    }
