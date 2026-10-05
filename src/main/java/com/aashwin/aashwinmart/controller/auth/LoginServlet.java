package com.aashwin.aashwinmart.controller.auth;

import com.aashwin.aashwinmart.exception.AppException;
import com.aashwin.aashwinmart.model.Role;
import com.aashwin.aashwinmart.model.User;
import com.aashwin.aashwinmart.service.AuthService;
import com.aashwin.aashwinmart.service.AuthServiceImpl;
import com.aashwin.aashwinmart.util.SecurityUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller handling user login and session establishment.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private final AuthService authService;

    public LoginServlet() {
        this.authService = new AuthServiceImpl();
    }

    public LoginServlet(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (SecurityUtil.isAuthenticated(request)) {
            redirectBasedOnRole(request, response);
            return;
        }
        request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String redirect = request.getParameter("redirect");

        try {
            User user = authService.login(email, password);
            SecurityUtil.setAuthenticatedUser(request, user);

            if (redirect != null && !redirect.trim().isEmpty() && !redirect.contains("://")) {
                response.sendRedirect(request.getContextPath() + redirect);
            } else {
                redirectBasedOnRole(request, response);
            }
        } catch (AppException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
        }
    }

    private void redirectBasedOnRole(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = SecurityUtil.getAuthenticatedUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        if (Role.ADMIN.equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        } else if (Role.SELLER.equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/seller/dashboard");
        } else {
            response.sendRedirect(request.getContextPath() + "/buyer/dashboard");
        }
    }
}
