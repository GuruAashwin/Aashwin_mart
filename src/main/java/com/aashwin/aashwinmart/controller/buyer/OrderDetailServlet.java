package com.aashwin.aashwinmart.controller.buyer;

import com.aashwin.aashwinmart.dto.OrderDTO;
import com.aashwin.aashwinmart.exception.AppException;
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

/**
 * Controller displaying detailed breakdown of a specific Order (F6).
 */
@WebServlet(name = "OrderDetailServlet", urlPatterns = {"/order/detail", "/buyer/order/detail"})
public class OrderDetailServlet extends HttpServlet {

    private final OrderService orderService;

    public OrderDetailServlet() {
        this.orderService = new OrderServiceImpl();
    }

    public OrderDetailServlet(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = SecurityUtil.getAuthenticatedUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/orders");
            return;
        }

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }

        try {
            Long orderId = Long.parseLong(idStr.trim());
            OrderDTO order = orderService.getOrderById(orderId, user.getId(), user.getRole());
            request.setAttribute("order", order);
            request.getRequestDispatcher("/WEB-INF/views/buyer/order-detail.jsp").forward(request, response);
        } catch (NumberFormatException | AppException e) {
            response.sendRedirect(request.getContextPath() + "/orders?error=notFound");
        }
    }
}
