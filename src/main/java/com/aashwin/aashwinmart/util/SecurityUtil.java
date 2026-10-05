package com.aashwin.aashwinmart.util;

import com.aashwin.aashwinmart.model.Role;
import com.aashwin.aashwinmart.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Security helper for session management, XSS escaping, and authorization checks.
 */
public final class SecurityUtil {

    public static final String SESSION_USER = "currentUser";

    private SecurityUtil() {
        // Prevent instantiation
    }

    public static User getAuthenticatedUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (User) session.getAttribute(SESSION_USER);
    }

    public static boolean isAuthenticated(HttpServletRequest request) {
        return getAuthenticatedUser(request) != null;
    }

    public static boolean hasRole(HttpServletRequest request, Role requiredRole) {
        User user = getAuthenticatedUser(request);
        return user != null && user.getRole() == requiredRole;
    }

    public static void setAuthenticatedUser(HttpServletRequest request, User user) {
        // Preserve CSRF token across session fixation prevention
        HttpSession oldSession = request.getSession(false);
        String csrfToken = null;
        if (oldSession != null) {
            csrfToken = (String) oldSession.getAttribute("_csrfToken");
            oldSession.invalidate();
        }
        HttpSession newSession = request.getSession(true);
        newSession.setMaxInactiveInterval(30 * 60); // 30 minutes explicit timeout
        newSession.setAttribute(SESSION_USER, user);
        // Restore or generate a fresh CSRF token for the new session
        if (csrfToken != null && !csrfToken.isEmpty()) {
            newSession.setAttribute("_csrfToken", csrfToken);
        }
    }

    public static void clearSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public static String escapeHtml(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
}
