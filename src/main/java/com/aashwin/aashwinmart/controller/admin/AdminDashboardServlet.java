package com.aashwin.aashwinmart.controller.admin;

import com.aashwin.aashwinmart.dto.OrderDTO;
import com.aashwin.aashwinmart.dto.UserDTO;
import com.aashwin.aashwinmart.model.OrderStatus;
import com.aashwin.aashwinmart.service.OrderService;
import com.aashwin.aashwinmart.service.OrderServiceImpl;
import com.aashwin.aashwinmart.service.ProductService;
import com.aashwin.aashwinmart.service.ProductServiceImpl;
import com.aashwin.aashwinmart.service.UserService;
import com.aashwin.aashwinmart.service.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Controller rendering the master Admin Dashboard with marketplace KPIs (F7).
 */
@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin/dashboard"})
public class AdminDashboardServlet extends HttpServlet {

    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;

    public AdminDashboardServlet() {
        this.userService = new UserServiceImpl();
        this.productService = new ProductServiceImpl();
        this.orderService = new OrderServiceImpl();
    }

    public AdminDashboardServlet(UserService userService, ProductService productService, OrderService orderService) {
        this.userService = userService;
        this.productService = productService;
        this.orderService = orderService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long totalUsers = userService.countAllUsers();
        long totalProducts = productService.countAllProducts();
        long totalOrders = orderService.countAllOrders();
        BigDecimal grossRevenue = orderService.getTotalRevenue();
        long pendingOrders = orderService.countOrdersByStatus(OrderStatus.PENDING);

        List<OrderDTO> recentOrders = orderService.getAllOrdersForAdmin().stream().limit(5).toList();
        List<UserDTO> recentUsers = userService.getAllUsers().stream().limit(5).toList();

        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("totalProducts", totalProducts);
        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("grossRevenue", grossRevenue);
        request.setAttribute("pendingOrders", pendingOrders);
        request.setAttribute("recentOrders", recentOrders);
        request.setAttribute("recentUsers", recentUsers);

        request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
    }
}
