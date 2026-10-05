package com.aashwin.aashwinmart.service;

import com.aashwin.aashwinmart.dto.CartItemDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for Shopping Cart operations (F4).
 */
public interface CartService {

    List<CartItemDTO> getCartForUser(Long userId);

    BigDecimal calculateCartTotal(Long userId);

    CartItemDTO addToCart(Long userId, Long productId, int quantity);

    boolean updateQuantity(Long userId, Long cartItemId, int newQuantity);

    boolean removeFromCart(Long userId, Long cartItemId);

    boolean clearCart(Long userId);

    int getCartCount(Long userId);
}
