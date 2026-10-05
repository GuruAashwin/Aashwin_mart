package com.aashwin.aashwinmart.controller.buyer;

import com.aashwin.aashwinmart.dto.CartItemDTO;
import com.aashwin.aashwinmart.exception.AppException;
import com.aashwin.aashwinmart.model.User;
import com.aashwin.aashwinmart.service.CartService;
import com.aashwin.aashwinmart.service.CartServiceImpl;
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
 * Controller handling Shopping Cart view and standard form actions (F4).
 */
@WebServlet(name = "CartServlet", urlPatterns = {"/cart", "/cart/add", "/cart/update", "/cart/remove", "/cart/clear"})
public class CartServlet extends HttpServlet {

    private final CartService cartService;

    public CartServlet() {
        this.cartService = new CartServiceImpl();
    }

    public CartServlet(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = SecurityUtil.getAuthenticatedUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/cart");
            return;
        }

        List<CartItemDTO> cartItems = cartService.getCartForUser(user.getId());
        BigDecimal cartTotal = cartService.calculateCartTotal(user.getId());

        request.setAttribute("cartItems", cartItems);
        request.setAttribute("cartTotal", cartTotal);
        request.getRequestDispatcher("/WEB-INF/views/buyer/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = SecurityUtil.getAuthenticatedUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/cart");
            return;
        }

        String path = request.getServletPath();

        try {
            if ("/cart/add".equals(path)) {
                Long productId = Long.parseLong(request.getParameter("productId"));
                int quantity = 1;
                String qtyStr = request.getParameter("quantity");
                if (qtyStr != null && !qtyStr.trim().isEmpty()) {
                    quantity = Math.max(1, Integer.parseInt(qtyStr.trim()));
                }
                cartService.addToCart(user.getId(), productId, quantity);
                String returnUrl = request.getParameter("returnUrl");
                if (returnUrl != null && !returnUrl.trim().isEmpty() && !returnUrl.contains("://")) {
                    response.sendRedirect(request.getContextPath() + returnUrl + (returnUrl.contains("?") ? "&" : "?") + "addedToCart=true");
                } else {
                    response.sendRedirect(request.getContextPath() + "/cart?added=true");
                }
                return;
            } else if ("/cart/update".equals(path)) {
                Long cartItemId = Long.parseLong(request.getParameter("cartItemId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                cartService.updateQuantity(user.getId(), cartItemId, quantity);
            } else if ("/cart/remove".equals(path)) {
                Long cartItemId = Long.parseLong(request.getParameter("cartItemId"));
                cartService.removeFromCart(user.getId(), cartItemId);
            } else if ("/cart/clear".equals(path)) {
                cartService.clearCart(user.getId());
            }
        } catch (AppException e) {
            request.getSession().setAttribute("cartError", e.getMessage());
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("cartError", "Invalid cart parameter.");
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }
}
