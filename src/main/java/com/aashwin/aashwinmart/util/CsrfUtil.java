package com.aashwin.aashwinmart.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * CSRF token utility for server-side token generation and validation.
 * Uses a cryptographically secure random token stored in the HTTP session.
 */
public final class CsrfUtil {

    public static final String CSRF_TOKEN_ATTR = "_csrfToken";
    public static final String CSRF_PARAM = "_csrf";
    public static final String CSRF_HEADER = "X-CSRF-Token";

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int TOKEN_BYTES = 32;

    private CsrfUtil() {
    }

    /**
     * Returns the existing CSRF token for the session, generating a new one if none exists.
     *
     * @param request HTTP request
     * @return CSRF token string
     */
    public static String getOrCreateToken(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        String token = (String) session.getAttribute(CSRF_TOKEN_ATTR);
        if (token == null || token.isEmpty()) {
            token = generateToken();
            session.setAttribute(CSRF_TOKEN_ATTR, token);
        }
        return token;
    }

    /**
     * Validates that the submitted CSRF token matches the session token.
     * Accepts token from form parameter or X-CSRF-Token header (AJAX).
     *
     * @param request HTTP request
     * @return true if token is valid
     */
    public static boolean isValidToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        String sessionToken = (String) session.getAttribute(CSRF_TOKEN_ATTR);
        if (sessionToken == null || sessionToken.isEmpty()) {
            return false;
        }

        // Check form parameter first, then header (for AJAX)
        String submittedToken = request.getParameter(CSRF_PARAM);
        if (submittedToken == null || submittedToken.isEmpty()) {
            submittedToken = request.getHeader(CSRF_HEADER);
        }

        return sessionToken.equals(submittedToken);
    }

    private static String generateToken() {
        byte[] bytes = new byte[TOKEN_BYTES];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
