package com.example.PayrollService.exception;

import java.io.Serial;

public class ValidationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ValidationException(String message) {
        super(message);
    }

//    public ValidationException(String message, Throwable cause) {
//        super(message, cause);
//    }
//
//    public ValidationException(String format, Object... args) {
//        super(String.format(format, args));
//    }
//
//    public ValidationException(Throwable cause, String format, Object... args) {
//        super(String.format(format, args), cause);
//    }
}