package com.aashwin.aashwinmart.exception;

/**
 * Base application exception for AASHWIN MART.
 */
public class AppException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final String errorCode;
    private final int httpStatus;

    public AppException(String message) {
        super(message);
        this.errorCode = "INTERNAL_SERVER_ERROR";
        this.httpStatus = 500;
    }

    public AppException(String errorCode, String message, int httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public AppException(String errorCode, String message, int httpStatus, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
