package com.aashwin.aashwinmart.controller.admin;

import com.aashwin.aashwinmart.dto.ProductDTO;
import com.aashwin.aashwinmart.service.ProductService;
import com.aashwin.aashwinmart.service.ProductServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Controller allowing Administrators to moderate, deactivate, or delete inappropriate marketplace listings (F7).
 */
@WebServlet(name = "AdminProductsServlet", urlPatterns = {"/admin/products", "/admin/products/delete", "/admin/products/toggle"})
public class AdminProductsServlet extends HttpServlet {

    private final ProductService productService;

    public AdminProductsServlet() {
        this.productService = new ProductServiceImpl();
    }

    public AdminProductsServlet(ProductService productService) {
        this.productService = productService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<ProductDTO> products = productService.getAllProductsForAdmin();
        request.setAttribute("products", products);
        request.getRequestDispatcher("/WEB-INF/views/admin/products.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String path = request.getServletPath();
        String productIdStr = request.getParameter("id");

        try {
            Long productId = Long.parseLong(productIdStr);
            if ("/admin/products/delete".equals(path)) {
                productService.adminDeleteProduct(productId);
                response.sendRedirect(request.getContextPath() + "/admin/products?deleted=true");
            } else if ("/admin/products/toggle".equals(path)) {
                boolean active = Boolean.parseBoolean(request.getParameter("active"));
                productService.adminToggleActive(productId, active);
                response.sendRedirect(request.getContextPath() + "/admin/products?toggled=true");
            }
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=failed");
        }
    }
}
