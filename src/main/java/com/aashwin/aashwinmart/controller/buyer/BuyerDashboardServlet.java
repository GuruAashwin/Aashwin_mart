package com.aashwin.aashwinmart.controller.buyer;

import com.aashwin.aashwinmart.dto.OrderDTO;
import com.aashwin.aashwinmart.model.User;
import com.aashwin.aashwinmart.service.CartService;
import com.aashwin.aashwinmart.service.CartServiceImpl;
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
 * Controller rendering Buyer overview dashboard with recent orders and cart status.
 */
@WebServlet(name = "BuyerDashboardServlet", urlPatterns = {"/buyer/dashboard"})
public class BuyerDashboardServlet extends HttpServlet {

    private final OrderService orderService;
    private final CartService cartService;

    public BuyerDashboardServlet() {
        this.orderService = new OrderServiceImpl();
        this.cartService = new CartServiceImpl();
    }

    public BuyerDashboardServlet(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = SecurityUtil.getAuthenticatedUser(request);
        List<OrderDTO> orders = orderService.getBuyerOrders(user.getId());
        int cartCount = cartService.getCartCount(user.getId());

        request.setAttribute("recentOrders", orders.stream().limit(5).toList());
        request.setAttribute("totalOrdersCount", orders.size());
        request.setAttribute("cartCount", cartCount);
        request.getRequestDispatcher("/WEB-INF/views/buyer/dashboard.jsp").forward(request, response);
    }
}
