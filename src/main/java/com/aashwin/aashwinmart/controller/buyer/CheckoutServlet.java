package com.aashwin.aashwinmart.controller.buyer;

import com.aashwin.aashwinmart.dto.CartItemDTO;
import com.aashwin.aashwinmart.dto.CheckoutRequestDTO;
import com.aashwin.aashwinmart.dto.OrderDTO;
import com.aashwin.aashwinmart.exception.AppException;
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
import java.math.BigDecimal;
import java.util.List;

/**
 * Controller handling the luxury checkout experience, order summary review, and mock payment confirmation (F5).
 */
@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout", "/checkout/confirm"})
public class CheckoutServlet extends HttpServlet {

    private final CartService cartService;
    private final OrderService orderService;

    public CheckoutServlet() {
        this.cartService = new CartServiceImpl();
        this.orderService = new OrderServiceImpl();
    }

    public CheckoutServlet(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = SecurityUtil.getAuthenticatedUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/checkout");
            return;
        }

        List<CartItemDTO> cartItems = cartService.getCartForUser(user.getId());
        if (cartItems.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart?empty=true");
            return;
        }

        BigDecimal subtotal = cartService.calculateCartTotal(user.getId());
        BigDecimal shippingFee = BigDecimal.ZERO; // Free Royal Delivery
        BigDecimal total = subtotal.add(shippingFee);

        request.setAttribute("cartItems", cartItems);
        request.setAttribute("subtotal", subtotal);
        request.setAttribute("shippingFee", shippingFee);
        request.setAttribute("total", total);
        request.setAttribute("user", user);

        request.getRequestDispatcher("/WEB-INF/views/buyer/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = SecurityUtil.getAuthenticatedUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/checkout");
            return;
        }

        CheckoutRequestDTO checkoutReq = new CheckoutRequestDTO();
        checkoutReq.setFullName(request.getParameter("fullName"));
        checkoutReq.setAddressLine(request.getParameter("addressLine"));
        checkoutReq.setCity(request.getParameter("city"));
        checkoutReq.setState(request.getParameter("state"));
        checkoutReq.setPostalCode(request.getParameter("postalCode"));
        checkoutReq.setPhone(request.getParameter("phone"));
        checkoutReq.setPaymentMethod(request.getParameter("paymentMethod"));
        checkoutReq.setCardNumber(request.getParameter("cardNumber"));
        checkoutReq.setUpiId(request.getParameter("upiId"));

        try {
            OrderDTO order = orderService.checkout(user.getId(), checkoutReq);
            request.setAttribute("order", order);
            request.getRequestDispatcher("/WEB-INF/views/buyer/order-confirmation.jsp").forward(request, response);
        } catch (AppException e) {
            List<CartItemDTO> cartItems = cartService.getCartForUser(user.getId());
            BigDecimal subtotal = cartService.calculateCartTotal(user.getId());
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("cartItems", cartItems);
            request.setAttribute("subtotal", subtotal);
            request.setAttribute("total", subtotal);
            request.setAttribute("checkoutReq", checkoutReq);
            request.getRequestDispatcher("/WEB-INF/views/buyer/checkout.jsp").forward(request, response);
        }
    }
}
