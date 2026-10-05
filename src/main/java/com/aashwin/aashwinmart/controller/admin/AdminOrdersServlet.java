package com.aashwin.aashwinmart.controller.admin;

import com.aashwin.aashwinmart.dto.OrderDTO;
import com.aashwin.aashwinmart.exception.AppException;
import com.aashwin.aashwinmart.model.OrderStatus;
import com.aashwin.aashwinmart.model.Role;
import com.aashwin.aashwinmart.model.User;
import com.aashwin.aashwinmart.service.OrderService;
import com.aashwin.aashwinmart.service.OrderServiceImpl;
import com.aashwin.aashwinmart.util.SecurityUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Controller displaying and managing all orders across the entire marketplace for Administrators (F7).
 */
@WebServlet(name = "AdminOrdersServlet", urlPatterns = {"/admin/orders", "/admin/orders/status"})
public class AdminOrdersServlet extends HttpServlet {

    private final OrderService orderService;

    public AdminOrdersServlet() {
        this.orderService = new OrderServiceImpl();
    }

    public AdminOrdersServlet(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<OrderDTO> orders = orderService.getAllOrdersForAdmin();
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/WEB-INF/views/admin/orders.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User admin = SecurityUtil.getAuthenticatedUser(request);
        String orderIdStr = request.getParameter("orderId");
        String statusStr = request.getParameter("status");

        try {
            Long orderId = Long.parseLong(orderIdStr);
            OrderStatus status = OrderStatus.fromString(statusStr);
            orderService.updateOrderStatus(orderId, status, admin.getId(), Role.ADMIN);
            response.sendRedirect(request.getContextPath() + "/admin/orders?statusUpdated=true");
        } catch (AppException e) {
            response.sendRedirect(request.getContextPath() + "/admin/orders?error=" + java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/orders");
        }
    }
}
