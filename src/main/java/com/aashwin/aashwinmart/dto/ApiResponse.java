package com.aashwin.aashwinmart.dto;

import java.io.Serializable;

/**
 * Standard JSON API envelope for all /api/v1/ endpoints.
 *
 * @param <T> Response payload data type
 */
public class ApiResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean success;
    private T data;
    private ApiError error;

    public ApiResponse() {
    }

    private ApiResponse(Builder<T> builder) {
        this.success = builder.success;
        this.data = builder.data;
        this.error = builder.error;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new Builder<T>().success(true).data(data).build();
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        return new Builder<T>().success(false).error(new ApiError(code, message)).build();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ApiError getError() {
        return error;
    }

    public void setError(ApiError error) {
        this.error = error;
    }

    public static class ApiError implements Serializable {
        private static final long serialVersionUID = 1L;
        private String code;
        private String message;

        public ApiError() {
        }

        public ApiError(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class Builder<T> {
        private boolean success;
        private T data;
        private ApiError error;

        public Builder<T> success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public Builder<T> error(ApiError error) {
            this.error = error;
            return this;
        }

        public ApiResponse<T> build() {
            return new ApiResponse<>(this);
        }
    }
}
