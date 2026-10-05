package com.aashwin.aashwinmart.filter;

import com.aashwin.aashwinmart.dto.ApiResponse;
import com.aashwin.aashwinmart.model.Role;
import com.aashwin.aashwinmart.model.User;
import com.aashwin.aashwinmart.util.JsonUtil;
import com.aashwin.aashwinmart.util.SecurityUtil;
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

/**
 * Filter enforcing Role-Based Access Control (RBAC) across Buyer, Seller, and Admin protected routes.
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = {"/buyer/*", "/seller/*", "/admin/*", "/cart/*", "/checkout/*", "/orders/*", "/review/*", "/api/v1/buyer/*", "/api/v1/seller/*", "/api/v1/admin/*"})
public class AuthFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String uri = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = uri.substring(contextPath.length());
        boolean isApiRequest = path.startsWith("/api/");

        User user = SecurityUtil.getAuthenticatedUser(httpRequest);

        // 1. Check Authentication
        if (user == null) {
            LOGGER.warn("Unauthenticated access attempt to protected route: {}", path);
            if (isApiRequest) {
                JsonUtil.writeJsonResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED,
                        ApiResponse.error("UNAUTHENTICATED", "Authentication required to access this resource."));
            } else {
                httpResponse.sendRedirect(contextPath + "/login?redirect=" + java.net.URLEncoder.encode(path, "UTF-8"));
            }
            return;
        }

        // 2. Check Authorization based on route prefix
        if (path.startsWith("/admin") || path.startsWith("/api/v1/admin")) {
            if (!Role.ADMIN.equals(user.getRole())) {
                handleForbidden(httpRequest, httpResponse, isApiRequest, path, "Administrator access required.");
                return;
            }
        } else if (path.startsWith("/seller") || path.startsWith("/api/v1/seller")) {
            if (!Role.SELLER.equals(user.getRole()) && !Role.ADMIN.equals(user.getRole())) {
                handleForbidden(httpRequest, httpResponse, isApiRequest, path, "Seller access required.");
                return;
            }
        } else if (path.startsWith("/buyer") || path.startsWith("/cart") || path.startsWith("/checkout") ||
                   path.startsWith("/orders") || path.startsWith("/review") || path.startsWith("/api/v1/buyer")) {
            if (!Role.BUYER.equals(user.getRole()) && !Role.ADMIN.equals(user.getRole())) {
                handleForbidden(httpRequest, httpResponse, isApiRequest, path, "Buyer access required.");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private void handleForbidden(HttpServletRequest request, HttpServletResponse response, boolean isApi, String path, String msg) throws IOException, ServletException {
        LOGGER.warn("Forbidden access attempt to {} by user ID {} role {}", path,
                SecurityUtil.getAuthenticatedUser(request).getId(),
                SecurityUtil.getAuthenticatedUser(request).getRole());
        if (isApi) {
            JsonUtil.writeJsonResponse(response, HttpServletResponse.SC_FORBIDDEN,
                    ApiResponse.error("FORBIDDEN", msg));
        } else {
            request.setAttribute("errorMessage", msg);
            request.getRequestDispatcher("/WEB-INF/views/error/403.jsp").forward(request, response);
        }
    }

    @Override
    public void destroy() {
    }
}
