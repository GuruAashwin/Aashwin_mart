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
 * Controller handling Seller onboarding registration.
 */
@WebServlet(name = "RegisterSellerServlet", urlPatterns = {"/register-seller"})
public class RegisterSellerServlet extends HttpServlet {

    private final AuthService authService;

    public RegisterSellerServlet() {
        this.authService = new AuthServiceImpl();
    }

    public RegisterSellerServlet(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (SecurityUtil.isAuthenticated(request)) {
            response.sendRedirect(request.getContextPath() + "/seller/dashboard");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/views/auth/register-seller.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        try {
            UserDTO userDTO = authService.registerSeller(name, email, password, confirmPassword);
            User user = authService.login(email, password);
            SecurityUtil.setAuthenticatedUser(request, user);

            response.sendRedirect(request.getContextPath() + "/seller/dashboard?registered=true");
        } catch (AppException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/views/auth/register-seller.jsp").forward(request, response);
        }
    }
}
