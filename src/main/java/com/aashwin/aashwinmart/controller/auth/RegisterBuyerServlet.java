package com.aashwin.aashwinmart.controller.auth;

import com.aashwin.aashwinmart.dto.UserDTO;
import com.aashwin.aashwinmart.exception.AppException;
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
 * Controller handling Buyer registration.
 */
@WebServlet(name = "RegisterBuyerServlet", urlPatterns = {"/register", "/register-buyer"})
public class RegisterBuyerServlet extends HttpServlet {

    private final AuthService authService;

    public RegisterBuyerServlet() {
        this.authService = new AuthServiceImpl();
    }

    public RegisterBuyerServlet(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (SecurityUtil.isAuthenticated(request)) {
            response.sendRedirect(request.getContextPath() + "/buyer/dashboard");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/views/auth/register-buyer.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        try {
            UserDTO userDTO = authService.registerBuyer(name, email, password, confirmPassword);
            // Automatically log the user in after successful registration
            User user = authService.login(email, password);
            SecurityUtil.setAuthenticatedUser(request, user);

            response.sendRedirect(request.getContextPath() + "/buyer/dashboard?registered=true");
        } catch (AppException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/views/auth/register-buyer.jsp").forward(request, response);
        }
    }
}
