package com.aashwin.aashwinmart.util;

import com.aashwin.aashwinmart.exception.ValidationException;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * Validation utility providing input validation and boundary enforcement.
 */
public final class ValidationUtil {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private ValidationUtil() {
        // Prevent instantiation
    }

    public static void requireNonEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " is required and cannot be empty");
        }
    }

    public static void validateEmail(String email) {
        requireNonEmpty(email, "Email");
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new ValidationException("Invalid email format: " + email);
        }
    }

    public static void validatePassword(String password) {
        requireNonEmpty(password, "Password");
        if (password.length() < 6) {
            throw new ValidationException("Password must be at least 6 characters in length");
        }
    }

    public static void validatePasswordConfirmation(String password, String confirmPassword) {
        validatePassword(password);
        if (!password.equals(confirmPassword)) {
            throw new ValidationException("Passwords do not match");
        }
    }

    public static void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Price must be greater than zero");
        }
        if (price.scale() > 2) {
            throw new ValidationException("Price cannot have more than 2 decimal places");
        }
    }

    public static void validateStockQty(Integer stockQty) {
        if (stockQty == null || stockQty < 0) {
            throw new ValidationException("Stock quantity cannot be negative");
        }
    }

    public static void validateRating(Integer rating) {
        if (rating == null || rating < 1 || rating > 5) {
            throw new ValidationException("Rating must be between 1 and 5 stars");
        }
    }

    public static void validateInputLength(String input, int maxLength, String fieldName) {
        if (input != null && input.length() > maxLength) {
            throw new ValidationException(fieldName + " exceeds maximum allowed length of " + maxLength + " characters");
        }
    }
}
