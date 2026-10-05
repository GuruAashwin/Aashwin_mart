package com.aashwin.aashwinmart.controller.seller;

import com.aashwin.aashwinmart.dto.OrderDTO;
import com.aashwin.aashwinmart.dto.ProductDTO;
import com.aashwin.aashwinmart.model.User;
import com.aashwin.aashwinmart.service.OrderService;
import com.aashwin.aashwinmart.service.OrderServiceImpl;
import com.aashwin.aashwinmart.service.ProductService;
import com.aashwin.aashwinmart.service.ProductServiceImpl;
import com.aashwin.aashwinmart.util.SecurityUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Controller rendering the Seller Analytics & Performance Dashboard (F2, O3).
 */
@WebServlet(name = "SellerDashboardServlet", urlPatterns = {"/seller/dashboard"})
public class SellerDashboardServlet extends HttpServlet {

    private final ProductService productService;
    private final OrderService orderService;

    public SellerDashboardServlet() {
        this.productService = new ProductServiceImpl();
        this.orderService = new OrderServiceImpl();
    }

    public SellerDashboardServlet(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User seller = SecurityUtil.getAuthenticatedUser(request);
        if (seller == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/seller/dashboard");
            return;
        }

        List<ProductDTO> products = productService.getSellerProducts(seller.getId());
        List<OrderDTO> incomingOrders = orderService.getSellerIncomingOrders(seller.getId());
        BigDecimal totalRevenue = orderService.getSellerRevenue(seller.getId());

        long activeListings = products.stream().filter(p -> Boolean.TRUE.equals(p.getActive())).count();
        long lowStockCount = products.stream().filter(p -> p.getStockQty() != null && p.getStockQty() <= 5).count();

        request.setAttribute("products", products);
        request.setAttribute("incomingOrders", incomingOrders);
        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("activeListings", activeListings);
        request.setAttribute("lowStockCount", lowStockCount);
        request.setAttribute("totalOrdersCount", incomingOrders.size());

        request.getRequestDispatcher("/WEB-INF/views/seller/dashboard.jsp").forward(request, response);
    }
}
