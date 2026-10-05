package com.aashwin.aashwinmart.controller.admin;

import com.aashwin.aashwinmart.dto.UserDTO;
import com.aashwin.aashwinmart.model.User;
import com.aashwin.aashwinmart.service.UserService;
import com.aashwin.aashwinmart.service.UserServiceImpl;
import com.aashwin.aashwinmart.util.SecurityUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Controller for Admin user management - view, deactivate and reactivate users (F7).
 */
@WebServlet(name = "AdminUsersServlet", urlPatterns = {"/admin/users", "/admin/users/toggle"})
public class AdminUsersServlet extends HttpServlet {

    private final UserService userService;

    public AdminUsersServlet() {
        this.userService = new UserServiceImpl();
    }

    public AdminUsersServlet(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<UserDTO> users = userService.getAllUsers();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        User admin = SecurityUtil.getAuthenticatedUser(request);
        if (admin == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String path = request.getServletPath();
        String userIdStr = request.getParameter("userId");

        if ("/admin/users/toggle".equals(path)) {
            try {
                Long targetUserId = Long.parseLong(userIdStr.trim());

                // Prevent admin from deactivating their own account
                if (targetUserId.equals(admin.getId())) {
                    response.sendRedirect(request.getContextPath() + "/admin/users?error=cannotModifySelf");
                    return;
                }

                boolean activate = "true".equalsIgnoreCase(request.getParameter("active"));
                userService.setUserActive(targetUserId, activate);
                String status = activate ? "activated" : "deactivated";
                response.sendRedirect(request.getContextPath() + "/admin/users?status=" + status);
            } catch (Exception e) {
                response.sendRedirect(request.getContextPath() + "/admin/users?error=failed");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/users");
        }
    }
}
