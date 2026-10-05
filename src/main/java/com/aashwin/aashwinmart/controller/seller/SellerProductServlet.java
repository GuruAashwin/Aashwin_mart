package com.aashwin.aashwinmart.controller.seller;

import com.aashwin.aashwinmart.dto.ProductDTO;
import com.aashwin.aashwinmart.exception.AppException;
import com.aashwin.aashwinmart.model.User;
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
 * Controller managing Seller Product Listings CRUD (F2).
 */
@WebServlet(name = "SellerProductServlet", urlPatterns = {
        "/seller/products",
        "/seller/products/add",
        "/seller/products/edit",
        "/seller/products/delete"
})
public class SellerProductServlet extends HttpServlet {

    private final ProductService productService;

    public SellerProductServlet() {
        this.productService = new ProductServiceImpl();
    }

    public SellerProductServlet(ProductService productService) {
        this.productService = productService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User seller = SecurityUtil.getAuthenticatedUser(request);
        if (seller == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/seller/products");
            return;
        }

        String path = request.getServletPath();

        if ("/seller/products/add".equals(path)) {
            request.setAttribute("isEdit", false);
            request.setAttribute("categories", productService.getAllCategories());
            request.getRequestDispatcher("/WEB-INF/views/seller/product-form.jsp").forward(request, response);
            return;
        }

        if ("/seller/products/edit".equals(path)) {
            String idStr = request.getParameter("id");
            if (idStr == null) {
                response.sendRedirect(request.getContextPath() + "/seller/products");
                return;
            }
            try {
                Long productId = Long.parseLong(idStr);
                ProductDTO product = productService.getProductById(productId);
                if (!seller.getId().equals(product.getSellerId())) {
                    response.sendRedirect(request.getContextPath() + "/seller/products?error=unauthorized");
                    return;
                }
                request.setAttribute("isEdit", true);
                request.setAttribute("product", product);
                request.setAttribute("categories", productService.getAllCategories());
                request.getRequestDispatcher("/WEB-INF/views/seller/product-form.jsp").forward(request, response);
                return;
            } catch (Exception e) {
                response.sendRedirect(request.getContextPath() + "/seller/products?error=notFound");
                return;
            }
        }

        // List all products for this seller
        List<ProductDTO> products = productService.getSellerProducts(seller.getId());
        request.setAttribute("products", products);
        request.getRequestDispatcher("/WEB-INF/views/seller/products.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User seller = SecurityUtil.getAuthenticatedUser(request);
        if (seller == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String path = request.getServletPath();

        if ("/seller/products/delete".equals(path)) {
            try {
                Long productId = Long.parseLong(request.getParameter("id"));
                productService.deleteProduct(seller.getId(), productId);
                response.sendRedirect(request.getContextPath() + "/seller/products?deleted=true");
            } catch (AppException e) {
                response.sendRedirect(request.getContextPath() + "/seller/products?error=" + java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
            }
            return;
        }

        // Create or Edit Product
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String priceStr = request.getParameter("price");
        String stockStr = request.getParameter("stockQty");
        String category = request.getParameter("category");
        String imageUrl = request.getParameter("imageUrl");
        String activeStr = request.getParameter("active");
        boolean active = "on".equalsIgnoreCase(activeStr) || "true".equalsIgnoreCase(activeStr);

        try {
            BigDecimal price = new BigDecimal(priceStr.trim());
            int stockQty = Integer.parseInt(stockStr.trim());

            if ("/seller/products/edit".equals(path)) {
                Long productId = Long.parseLong(request.getParameter("id"));
                productService.updateProduct(seller.getId(), productId, name, description, price, stockQty, category, imageUrl, active);
                response.sendRedirect(request.getContextPath() + "/seller/products?updated=true");
            } else {
                productService.createProduct(seller.getId(), name, description, price, stockQty, category, imageUrl);
                response.sendRedirect(request.getContextPath() + "/seller/products?created=true");
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("isEdit", "/seller/products/edit".equals(path));
            request.setAttribute("categories", productService.getAllCategories());
            request.getRequestDispatcher("/WEB-INF/views/seller/product-form.jsp").forward(request, response);
        }
    }
}
