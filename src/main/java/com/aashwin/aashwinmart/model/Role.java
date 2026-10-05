package com.aashwin.aashwinmart.model;

/**
 * User role enumeration representing authorization tiers in AASHWIN MART.
 */
public enum Role {
    BUYER,
    SELLER,
    ADMIN;

    public static Role fromString(String roleStr) {
        if (roleStr == null) {
            return BUYER;
        }
        try {
            return Role.valueOf(roleStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return BUYER;
        }
    }
}
