package com.aashwin.aashwinmart.exception;

/**
 * Thrown when an authenticated user attempts to access a forbidden resource. (HTTP 403)
 */
public class AuthorizationException extends AppException {
    private static final long serialVersionUID = 1L;

    public AuthorizationException(String message) {
        super("FORBIDDEN", message, 403);
    }
}
