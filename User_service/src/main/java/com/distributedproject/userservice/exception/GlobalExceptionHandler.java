package com.distributedproject.userservice.exception;

import com.distributedproject.userservice.exception.department.DepartmentNameAlreadyExistsException;
import com.distributedproject.userservice.exception.department.DepartmentNameNotFoundException;
import com.distributedproject.userservice.exception.department.DepartmentNotFoundException;
import com.distributedproject.userservice.exception.role.RoleNameAlreadyExistsException;
import com.distributedproject.userservice.exception.role.RoleNameNotFoundException;
import com.distributedproject.userservice.exception.role.RoleNotFoundException;
import com.distributedproject.userservice.exception.user.UserIdAlreadyExistsException;
import com.distributedproject.userservice.exception.user.UserNameAlreadyExistsException;
import com.distributedproject.userservice.exception.user.UserNameNotFoundException;
import com.distributedproject.userservice.exception.user.UserNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUsersNotFoundException(UserNameNotFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("errorMessage", ex.getMessage());
        error.put("error", "Not Found");
        error.put("statusCode", HttpStatus.NOT_FOUND.value());
        error.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(UserNotFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("errorMessage", ex.getMessage());
        error.put("error", "Not Found");
        error.put("statusCode", HttpStatus.NOT_FOUND.value());
        error.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DepartmentNameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleDepartmentsNotFoundException(DepartmentNameNotFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("errorMessage", ex.getMessage());
        error.put("error", "Not Found");
        error.put("statusCode", HttpStatus.NOT_FOUND.value());
        error.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleDepartmentNotFound(com.distributedproject.userservice.exception.department.DepartmentNotFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("errorMessage", ex.getMessage());
        error.put("error", "Not Found");
        error.put("statusCode", HttpStatus.NOT_FOUND.value());
        error.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoleNameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleRoleNotFoundException(RoleNameNotFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("errorMessage", ex.getMessage());
        error.put("error", "Not Found");
        error.put("statusCode", HttpStatus.NOT_FOUND.value());
        error.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleRoleNotFound(RoleNotFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("errorMessage", ex.getMessage());
        error.put("error", "Not Found");
        error.put("statusCode", HttpStatus.NOT_FOUND.value());
        error.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", new Date());
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        errorResponse.put("errors", errors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("errorMessage", ex.getMessage());
        error.put("error", "Not Found");
        error.put("statusCode", HttpStatus.NOT_FOUND.value());
        error.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DepartmentNameAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleDepartmentNameAlreadyExistsException(DepartmentNameAlreadyExistsException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("errorMessage", ex.getMessage());
        error.put("error", "Already Exits");
        error.put("statusCode", HttpStatus.NOT_FOUND.value());
        error.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNameAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUserNameAlreadyExistsException(UserNameAlreadyExistsException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("errorMessage", ex.getMessage());
        error.put("error", "Already Exits");
        error.put("statusCode", HttpStatus.NOT_FOUND.value());
        error.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserIdAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUserIdAlreadyExistsException(UserIdAlreadyExistsException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("errorMessage", ex.getMessage());
        error.put("error", "Already Exits");
        error.put("statusCode", HttpStatus.NOT_FOUND.value());
        error.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoleNameAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleRoleNameAlreadyExistsException(RoleNameAlreadyExistsException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("errorMessage", ex.getMessage());
        error.put("error", "Already Exits");
        error.put("statusCode", HttpStatus.NOT_FOUND.value());
        error.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }



}

