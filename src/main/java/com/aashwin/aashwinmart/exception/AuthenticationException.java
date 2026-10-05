package com.aashwin.aashwinmart.exception;

/**
 * Thrown when user credentials or session authentication fails. (HTTP 401)
 */
public class AuthenticationException extends AppException {
    private static final long serialVersionUID = 1L;

    public AuthenticationException(String message) {
        super("UNAUTHENTICATED", message, 401);
    }
}
