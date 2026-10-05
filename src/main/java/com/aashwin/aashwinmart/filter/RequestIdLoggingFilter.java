package com.aashwin.aashwinmart.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

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
import java.util.UUID;

/**
 * Filter generating a unique request ID for observability, putting it in SLF4J MDC, and attaching X-Request-Id header.
 */
@WebFilter(filterName = "RequestIdLoggingFilter", urlPatterns = "/*")
public class RequestIdLoggingFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestIdLoggingFilter.class);
    private static final String MDC_KEY = "requestId";
    private static final String HEADER_NAME = "X-Request-Id";

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestId = httpRequest.getHeader(HEADER_NAME);
        if (requestId == null || requestId.trim().isEmpty()) {
            requestId = "req-" + UUID.randomUUID().toString().substring(0, 8);
        }

        MDC.put(MDC_KEY, requestId);
        httpResponse.setHeader(HEADER_NAME, requestId);

        long startTime = System.currentTimeMillis();
        try {
            LOGGER.debug("--> Incoming HTTP {} {}", httpRequest.getMethod(), httpRequest.getRequestURI());
            chain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            LOGGER.debug("<-- Completed HTTP {} {} [{}ms]", httpRequest.getMethod(), httpRequest.getRequestURI(), duration);
            MDC.remove(MDC_KEY);
        }
    }

    @Override
    public void destroy() {
    }
}
