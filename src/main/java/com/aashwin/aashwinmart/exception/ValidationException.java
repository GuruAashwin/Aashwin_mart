package com.aashwin.aashwinmart.exception;

/**
 * Thrown when client input fails business or format validation rules. (HTTP 400)
 */
public class ValidationException extends AppException {
    private static final long serialVersionUID = 1L;

    public ValidationException(String message) {
        super("VALIDATION_ERROR", message, 400);
    }
}
