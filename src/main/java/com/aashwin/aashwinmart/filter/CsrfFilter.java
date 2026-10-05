package com.aashwin.aashwinmart.filter;

import com.aashwin.aashwinmart.dto.ApiResponse;
import com.aashwin.aashwinmart.util.CsrfUtil;
import com.aashwin.aashwinmart.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * CSRF protection filter for all state-changing POST requests.
 * GET/HEAD/OPTIONS are exempt (safe methods per RFC 7231).
 * JSON API endpoints accept the CSRF token via X-CSRF-Token header.
 */
@WebFilter(filterName = "CsrfFilter", urlPatterns = "/*")
public class CsrfFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsrfFilter.class);

    private static final Set<String> SAFE_METHODS = new HashSet<>(Arrays.asList("GET", "HEAD", "OPTIONS", "TRACE"));

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String method = httpRequest.getMethod();

        // Ensure a CSRF token exists in the session for all requests
        CsrfUtil.getOrCreateToken(httpRequest);

        // Only validate on state-changing methods
        if (!SAFE_METHODS.contains(method.toUpperCase())) {
            if (!CsrfUtil.isValidToken(httpRequest)) {
                String path = httpRequest.getRequestURI();
                boolean isApiRequest = path.contains("/api/");

                LOGGER.warn("CSRF token validation failed for {} {}", method, path);

                if (isApiRequest) {
                    JsonUtil.writeJsonResponse(httpResponse, HttpServletResponse.SC_FORBIDDEN,
                            ApiResponse.error("CSRF_INVALID", "Invalid or missing CSRF token."));
                } else {
                    httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    request.setAttribute("errorMessage", "Security validation failed. Please refresh the page and try again.");
                    request.getRequestDispatcher("/WEB-INF/views/error/403.jsp").forward(request, response);
                }
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
