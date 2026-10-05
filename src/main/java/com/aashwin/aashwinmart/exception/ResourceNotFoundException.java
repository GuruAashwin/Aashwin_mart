package com.aashwin.aashwinmart.exception;

/**
 * Thrown when a requested resource (product, order, user) cannot be found. (HTTP 404)
 */
public class ResourceNotFoundException extends AppException {
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super("RESOURCE_NOT_FOUND", message, 404);
    }
}
