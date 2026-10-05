package com.aashwin.aashwinmart.exception;

/**
 * Thrown during checkout or cart operations when requested quantity exceeds available inventory. (HTTP 409)
 */
public class InsufficientStockException extends AppException {
    private static final long serialVersionUID = 1L;

    public InsufficientStockException(String message) {
        super("INSUFFICIENT_STOCK", message, 409);
    }
}
