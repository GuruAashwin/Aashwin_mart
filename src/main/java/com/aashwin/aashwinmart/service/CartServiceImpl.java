package com.aashwin.aashwinmart.service;

import com.aashwin.aashwinmart.dao.CartItemDAO;
import com.aashwin.aashwinmart.dao.DAOFactory;
import com.aashwin.aashwinmart.dao.ProductDAO;
import com.aashwin.aashwinmart.dto.CartItemDTO;
import com.aashwin.aashwinmart.exception.InsufficientStockException;
import com.aashwin.aashwinmart.exception.ResourceNotFoundException;
import com.aashwin.aashwinmart.exception.ValidationException;
import com.aashwin.aashwinmart.model.CartItem;
import com.aashwin.aashwinmart.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of CartService with stock availability checks and subtotal calculations.
 */
public class CartServiceImpl implements CartService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartServiceImpl.class);

    private final CartItemDAO cartItemDAO;
    private final ProductDAO productDAO;

    public CartServiceImpl() {
        this.cartItemDAO = DAOFactory.getCartItemDAO();
        this.productDAO = DAOFactory.getProductDAO();
    }

    public CartServiceImpl(CartItemDAO cartItemDAO, ProductDAO productDAO) {
        this.cartItemDAO = cartItemDAO;
        this.productDAO = productDAO;
    }

    @Override
    public List<CartItemDTO> getCartForUser(Long userId) {
        if (userId == null) {
            return List.of();
        }
        return cartItemDAO.findByUserId(userId).stream()
                .map(CartItemDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal calculateCartTotal(Long userId) {
        List<CartItem> items = cartItemDAO.findByUserId(userId);
        return items.stream()
                .map(CartItem::getItemTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public CartItemDTO addToCart(Long userId, Long productId, int quantity) {
        if (userId == null || productId == null) {
            throw new IllegalArgumentException("User ID and Product ID are required");
        }
        if (quantity <= 0) {
            throw new ValidationException("Quantity must be at least 1");
        }

        Product product = productDAO.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new ValidationException("This product listing is no longer active");
        }

        if (product.getStockQty() <= 0) {
            throw new InsufficientStockException("Sorry, this item is currently out of stock");
        }

        // Check if item is already in cart
        Optional<CartItem> existing = cartItemDAO.findItem(userId, productId);
        int currentQtyInCart = existing.map(CartItem::getQuantity).orElse(0);
        int totalRequestedQty = currentQtyInCart + quantity;

        if (totalRequestedQty > product.getStockQty()) {
            throw new InsufficientStockException("Cannot add " + quantity + " more item(s). Available stock is only " +
                    product.getStockQty() + " (you already have " + currentQtyInCart + " in cart)");
        }

        CartItem saved = cartItemDAO.saveOrUpdate(userId, productId, quantity);
        LOGGER.info("Added product ID {} (qty: {}) to cart for user ID {}", productId, quantity, userId);

        // Fetch refreshed cart item with joined fields
        return cartItemDAO.findByUserId(userId).stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .map(CartItemDTO::fromEntity)
                .orElse(CartItemDTO.fromEntity(saved));
    }

    @Override
    public boolean updateQuantity(Long userId, Long cartItemId, int newQuantity) {
        if (userId == null || cartItemId == null) {
            return false;
        }
        if (newQuantity <= 0) {
            return removeFromCart(userId, cartItemId);
        }

        List<CartItem> userItems = cartItemDAO.findByUserId(userId);
        CartItem target = userItems.stream()
                .filter(i -> i.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        Product product = productDAO.findById(target.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (newQuantity > product.getStockQty()) {
            throw new InsufficientStockException("Requested quantity (" + newQuantity + ") exceeds available stock (" + product.getStockQty() + ")");
        }

        return cartItemDAO.updateQuantity(cartItemId, userId, newQuantity);
    }

    @Override
    public boolean removeFromCart(Long userId, Long cartItemId) {
        if (userId == null || cartItemId == null) {
            return false;
        }
        return cartItemDAO.removeItem(cartItemId, userId);
    }

    @Override
    public boolean clearCart(Long userId) {
        if (userId == null) {
            return false;
        }
        return cartItemDAO.clearCart(userId);
    }

    @Override
    public int getCartCount(Long userId) {
        if (userId == null) {
            return 0;
        }
        return cartItemDAO.countItemsByUser(userId);
    }
}
