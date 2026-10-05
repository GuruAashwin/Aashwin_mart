package com.aashwin.aashwinmart.controller.buyer;

import com.aashwin.aashwinmart.exception.AppException;
import com.aashwin.aashwinmart.model.User;
import com.aashwin.aashwinmart.service.ReviewService;
import com.aashwin.aashwinmart.service.ReviewServiceImpl;
import com.aashwin.aashwinmart.util.SecurityUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller handling verified Buyer review submission (F8).
 */
@WebServlet(name = "ReviewSubmitServlet", urlPatterns = {"/review/submit"})
public class ReviewSubmitServlet extends HttpServlet {

    private final ReviewService reviewService;

    public ReviewSubmitServlet() {
        this.reviewService = new ReviewServiceImpl();
    }

    public ReviewSubmitServlet(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = SecurityUtil.getAuthenticatedUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String productIdStr = request.getParameter("productId");
        String ratingStr = request.getParameter("rating");
        String comment = request.getParameter("comment");

        try {
            Long productId = Long.parseLong(productIdStr.trim());
            int rating = Integer.parseInt(ratingStr.trim());

            reviewService.createReview(user.getId(), productId, rating, comment);
            response.sendRedirect(request.getContextPath() + "/product?id=" + productId + "&reviewSuccess=true");
        } catch (AppException e) {
            response.sendRedirect(request.getContextPath() + "/product?id=" + productIdStr + "&reviewError=" + java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/shop");
        }
    }
}
