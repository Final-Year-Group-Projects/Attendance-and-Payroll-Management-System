package com.distributedproject.authservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        logger.error("Error occurred during login: StatusCode: {}, Timestamp: {}, Message: {}",
                HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now(), ex.getMessage());

        Map<String, Object> error = new HashMap<>();
        error.put("errorMessage", ex.getMessage());
        error.put("error", "Unauthorized");
        error.put("statusCode", HttpStatus.UNAUTHORIZED.value());
        error.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {

        Map<String, Object> response = new HashMap<>();

        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");
        logger.error("Error occurred while creating user: StatusCode: {}, Timestamp: {}, Message: {}", 404, LocalDateTime.now(), errorMessage);

        response.put("errorMessage", errorMessage);
        response.put("error", "Invalid error");
        response.put("statusCode", 404);
        response.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUsernameAlreadyExists(UsernameAlreadyExistsException ex) {
        // Log error details
        logger.error("Error occurred while creating user: StatusCode: {}, Timestamp: {}, Message: {}",
                HttpStatus.CONFLICT.value(), LocalDateTime.now(), ex.getMessage());

        // Prepare the error response
        Map<String, Object> error = new HashMap<>();
        error.put("errorMessage", ex.getMessage());
        error.put("error", "Already Exists");
        error.put("statusCode", HttpStatus.CONFLICT.value());
        error.put("timestamp", LocalDateTime.now());

        // Return the error response with a 409 status
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}
