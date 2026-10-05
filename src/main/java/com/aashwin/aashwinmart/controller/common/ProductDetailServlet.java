package com.aashwin.aashwinmart.controller.common;

import com.aashwin.aashwinmart.dto.ProductDTO;
import com.aashwin.aashwinmart.dto.ReviewDTO;
import com.aashwin.aashwinmart.exception.AppException;
import com.aashwin.aashwinmart.model.User;
import com.aashwin.aashwinmart.service.ProductService;
import com.aashwin.aashwinmart.service.ProductServiceImpl;
import com.aashwin.aashwinmart.service.ReviewService;
import com.aashwin.aashwinmart.service.ReviewServiceImpl;
import com.aashwin.aashwinmart.util.SecurityUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Controller displaying detailed product information, high-res imagery, verified buyer reviews, and rating stats.
 */
@WebServlet(name = "ProductDetailServlet", urlPatterns = {"/product"})
public class ProductDetailServlet extends HttpServlet {

    private final ProductService productService;
    private final ReviewService reviewService;

    public ProductDetailServlet() {
        this.productService = new ProductServiceImpl();
        this.reviewService = new ReviewServiceImpl();
    }

    public ProductDetailServlet(ProductService productService, ReviewService reviewService) {
        this.productService = productService;
        this.reviewService = reviewService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/shop");
            return;
        }

        try {
            Long productId = Long.parseLong(idStr);
            ProductDTO product = productService.getProductById(productId);
            List<ReviewDTO> reviews = reviewService.getProductReviews(productId);

            User currentUser = SecurityUtil.getAuthenticatedUser(request);
            boolean isEligibleToReview = false;
            if (currentUser != null && currentUser.isBuyer()) {
                isEligibleToReview = reviewService.isEligibleToReview(currentUser.getId(), productId);
            }

            request.setAttribute("product", product);
            request.setAttribute("reviews", reviews);
            request.setAttribute("isEligibleToReview", isEligibleToReview);
            request.getRequestDispatcher("/WEB-INF/views/product-detail.jsp").forward(request, response);
        } catch (NumberFormatException | AppException e) {
            response.sendRedirect(request.getContextPath() + "/shop?error=notFound");
        }
    }
}
