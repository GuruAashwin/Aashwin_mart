package com.aashwin.aashwinmart.controller.seller;

import com.aashwin.aashwinmart.dto.OrderDTO;
import com.aashwin.aashwinmart.exception.AppException;
import com.aashwin.aashwinmart.model.OrderStatus;
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
 * Controller managing incoming customer orders for products sold by the authenticated Seller (F6, O2).
 */
@WebServlet(name = "SellerOrdersServlet", urlPatterns = {"/seller/orders", "/seller/orders/status"})
public class SellerOrdersServlet extends HttpServlet {

    private final OrderService orderService;

    public SellerOrdersServlet() {
        this.orderService = new OrderServiceImpl();
    }

    public SellerOrdersServlet(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User seller = SecurityUtil.getAuthenticatedUser(request);
        if (seller == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/seller/orders");
            return;
        }

        List<OrderDTO> incomingOrders = orderService.getSellerIncomingOrders(seller.getId());
        request.setAttribute("orders", incomingOrders);
        request.getRequestDispatcher("/WEB-INF/views/seller/orders.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User seller = SecurityUtil.getAuthenticatedUser(request);
        if (seller == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String orderIdStr = request.getParameter("orderId");
        String statusStr = request.getParameter("status");

        try {
            Long orderId = Long.parseLong(orderIdStr);
            OrderStatus status = OrderStatus.fromString(statusStr);
            orderService.updateOrderStatus(orderId, status, seller.getId(), seller.getRole());
            response.sendRedirect(request.getContextPath() + "/seller/orders?statusUpdated=true");
        } catch (AppException e) {
            response.sendRedirect(request.getContextPath() + "/seller/orders?error=" + java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/seller/orders");
        }
    }
}
