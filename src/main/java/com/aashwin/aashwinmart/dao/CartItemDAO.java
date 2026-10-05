package com.aashwin.aashwinmart.dao;

import com.aashwin.aashwinmart.model.CartItem;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Shopping Cart management.
 */
public interface CartItemDAO {

    List<CartItem> findByUserId(Long userId);

    Optional<CartItem> findItem(Long userId, Long productId);

    CartItem saveOrUpdate(Long userId, Long productId, int quantity);

    boolean updateQuantity(Long cartItemId, Long userId, int newQuantity);

    boolean removeItem(Long cartItemId, Long userId);

    boolean clearCart(Connection conn, Long userId);

    boolean clearCart(Long userId);

    int countItemsByUser(Long userId);
}
