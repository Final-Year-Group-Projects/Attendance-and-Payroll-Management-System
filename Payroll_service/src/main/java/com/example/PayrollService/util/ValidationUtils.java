package com.example.PayrollService.util;

import com.example.PayrollService.exception.ValidationException;
import java.util.List;

public class ValidationUtils {

    public static void validateInList(
            String value,
            List<String> validOptions,
            String fieldName
    ) {
        if (!validOptions.contains(value.toUpperCase())) {
            throw new ValidationException(
                      String.format("Invalid %s: %s. Valid Status are %s", fieldName, value, validOptions)
            );
        }
    }
}