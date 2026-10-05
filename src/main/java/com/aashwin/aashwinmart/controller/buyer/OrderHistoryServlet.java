package com.aashwin.aashwinmart.controller.buyer;

import com.aashwin.aashwinmart.dto.OrderDTO;
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
 * Controller displaying Buyer order history (F6).
 */
@WebServlet(name = "OrderHistoryServlet", urlPatterns = {"/orders", "/buyer/orders"})
public class OrderHistoryServlet extends HttpServlet {

    private final OrderService orderService;

    public OrderHistoryServlet() {
        this.orderService = new OrderServiceImpl();
    }

    public OrderHistoryServlet(OrderService orderService) {
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

        List<OrderDTO> orders = orderService.getBuyerOrders(user.getId());
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/WEB-INF/views/buyer/orders.jsp").forward(request, response);
    }
}
