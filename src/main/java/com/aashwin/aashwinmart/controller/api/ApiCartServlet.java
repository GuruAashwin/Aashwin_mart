package com.aashwin.aashwinmart.controller.api;

import com.aashwin.aashwinmart.dto.ApiResponse;
import com.aashwin.aashwinmart.dto.CartItemDTO;
import com.aashwin.aashwinmart.exception.AppException;
import com.aashwin.aashwinmart.model.User;
import com.aashwin.aashwinmart.service.CartService;
import com.aashwin.aashwinmart.service.CartServiceImpl;
import com.aashwin.aashwinmart.util.JsonUtil;
import com.aashwin.aashwinmart.util.SecurityUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST API controller for AJAX Shopping Cart operations.
 * URL: /api/v1/cart/*
 */
@WebServlet(name = "ApiCartServlet", urlPatterns = {"/api/v1/cart", "/api/v1/cart/add", "/api/v1/cart/update", "/api/v1/cart/remove"})
public class ApiCartServlet extends HttpServlet {

    private final CartService cartService;

    public ApiCartServlet() {
        this.cartService = new CartServiceImpl();
    }

    public ApiCartServlet(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = SecurityUtil.getAuthenticatedUser(request);
        if (user == null) {
            JsonUtil.writeJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                    ApiResponse.error("UNAUTHENTICATED", "Authentication required."));
            return;
        }

        List<CartItemDTO> items = cartService.getCartForUser(user.getId());
        BigDecimal total = cartService.calculateCartTotal(user.getId());
        int count = cartService.getCartCount(user.getId());

        Map<String, Object> data = new HashMap<>();
        data.put("items", items);
        data.put("total", total);
        data.put("count", count);

        JsonUtil.writeJsonResponse(response, HttpServletResponse.SC_OK, ApiResponse.success(data));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = SecurityUtil.getAuthenticatedUser(request);
        if (user == null) {
            JsonUtil.writeJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                    ApiResponse.error("UNAUTHENTICATED", "Authentication required."));
            return;
        }

        String path = request.getServletPath();

        try {
            if ("/api/v1/cart/add".equals(path)) {
                Long productId = Long.parseLong(request.getParameter("productId"));
                int quantity = 1;
                if (request.getParameter("quantity") != null) {
                    quantity = Integer.parseInt(request.getParameter("quantity"));
                }
                CartItemDTO item = cartService.addToCart(user.getId(), productId, quantity);
                int cartCount = cartService.getCartCount(user.getId());
                BigDecimal cartTotal = cartService.calculateCartTotal(user.getId());

                Map<String, Object> data = new HashMap<>();
                data.put("item", item);
                data.put("cartCount", cartCount);
                data.put("cartTotal", cartTotal);

                JsonUtil.writeJsonResponse(response, HttpServletResponse.SC_OK, ApiResponse.success(data));
            } else if ("/api/v1/cart/update".equals(path)) {
                Long cartItemId = Long.parseLong(request.getParameter("cartItemId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                cartService.updateQuantity(user.getId(), cartItemId, quantity);

                int cartCount = cartService.getCartCount(user.getId());
                BigDecimal cartTotal = cartService.calculateCartTotal(user.getId());

                Map<String, Object> data = new HashMap<>();
                data.put("cartCount", cartCount);
                data.put("cartTotal", cartTotal);

                JsonUtil.writeJsonResponse(response, HttpServletResponse.SC_OK, ApiResponse.success(data));
            } else if ("/api/v1/cart/remove".equals(path)) {
                Long cartItemId = Long.parseLong(request.getParameter("cartItemId"));
                cartService.removeFromCart(user.getId(), cartItemId);

                int cartCount = cartService.getCartCount(user.getId());
                BigDecimal cartTotal = cartService.calculateCartTotal(user.getId());

                Map<String, Object> data = new HashMap<>();
                data.put("cartCount", cartCount);
                data.put("cartTotal", cartTotal);

                JsonUtil.writeJsonResponse(response, HttpServletResponse.SC_OK, ApiResponse.success(data));
            }
        } catch (AppException e) {
            JsonUtil.writeJsonResponse(response, e.getHttpStatus(), ApiResponse.error(e.getErrorCode(), e.getMessage()));
        } catch (Exception e) {
            JsonUtil.writeJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    ApiResponse.error("INTERNAL_SERVER_ERROR", "Cart action failed."));
        }
    }
}
