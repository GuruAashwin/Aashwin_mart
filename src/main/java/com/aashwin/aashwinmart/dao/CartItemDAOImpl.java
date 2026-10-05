package com.aashwin.aashwinmart.dao;

import com.aashwin.aashwinmart.config.DatabaseConfig;
import com.aashwin.aashwinmart.model.CartItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of CartItemDAO.
 */
public class CartItemDAOImpl implements CartItemDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartItemDAOImpl.class);

    private static final String SQL_FIND_BY_USER =
            "SELECT c.id, c.user_id, c.product_id, c.quantity, c.created_at, " +
            "       p.name AS product_name, p.description AS product_description, " +
            "       p.price AS product_price, p.stock_qty AS product_stock_qty, " +
            "       p.category AS product_category, p.image_url AS product_image_url, " +
            "       p.seller_id, u.name AS seller_name " +
            "FROM cart_items c " +
            "JOIN products p ON c.product_id = p.id " +
            "JOIN users u ON p.seller_id = u.id " +
            "WHERE c.user_id = ? " +
            "ORDER BY c.created_at DESC";

    private static final String SQL_FIND_ITEM =
            "SELECT id, user_id, product_id, quantity, created_at FROM cart_items WHERE user_id = ? AND product_id = ?";

    private static final String SQL_INSERT =
            "INSERT INTO cart_items (user_id, product_id, quantity, created_at) VALUES (?, ?, ?, ?)";

    private static final String SQL_UPDATE_QTY =
            "UPDATE cart_items SET quantity = ? WHERE id = ? AND user_id = ?";

    private static final String SQL_UPDATE_QTY_BY_USER_PROD =
            "UPDATE cart_items SET quantity = quantity + ? WHERE user_id = ? AND product_id = ?";

    private static final String SQL_REMOVE =
            "DELETE FROM cart_items WHERE id = ? AND user_id = ?";

    private static final String SQL_CLEAR_USER =
            "DELETE FROM cart_items WHERE user_id = ?";

    private static final String SQL_COUNT_USER =
            "SELECT COALESCE(SUM(quantity), 0) FROM cart_items WHERE user_id = ?";

    @Override
    public List<CartItem> findByUserId(Long userId) {
        List<CartItem> items = new ArrayList<>();
        if (userId == null) {
            return items;
        }
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_USER)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartItem item = new CartItem();
                    item.setId(rs.getLong("id"));
                    item.setUserId(rs.getLong("user_id"));
                    item.setProductId(rs.getLong("product_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setCreatedAt(rs.getTimestamp("created_at"));
                    item.setProductName(rs.getString("product_name"));
                    item.setProductDescription(rs.getString("product_description"));
                    item.setProductPrice(rs.getBigDecimal("product_price"));
                    item.setProductStockQty(rs.getInt("product_stock_qty"));
                    item.setProductCategory(rs.getString("product_category"));
                    item.setProductImageUrl(rs.getString("product_image_url"));
                    item.setSellerId(rs.getLong("seller_id"));
                    item.setSellerName(rs.getString("seller_name"));
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding cart items for user ID: {}", userId, e);
            throw new RuntimeException("Database error finding cart items", e);
        }
        return items;
    }

    @Override
    public Optional<CartItem> findItem(Long userId, Long productId) {
        if (userId == null || productId == null) {
            return Optional.empty();
        }
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ITEM)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    CartItem item = new CartItem();
                    item.setId(rs.getLong("id"));
                    item.setUserId(rs.getLong("user_id"));
                    item.setProductId(rs.getLong("product_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setCreatedAt(rs.getTimestamp("created_at"));
                    return Optional.of(item);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding cart item for user {} and product {}", userId, productId, e);
            throw new RuntimeException("Database error finding cart item", e);
        }
        return Optional.empty();
    }

    @Override
    public CartItem saveOrUpdate(Long userId, Long productId, int quantity) {
        Optional<CartItem> existing = findItem(userId, productId);
        if (existing.isPresent()) {
            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_QTY_BY_USER_PROD)) {
                ps.setInt(1, quantity);
                ps.setLong(2, userId);
                ps.setLong(3, productId);
                ps.executeUpdate();
                CartItem item = existing.get();
                item.setQuantity(item.getQuantity() + quantity);
                return item;
            } catch (SQLException e) {
                LOGGER.error("Error updating cart quantity", e);
                throw new RuntimeException("Database error updating cart item", e);
            }
        } else {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
                ps.setLong(1, userId);
                ps.setLong(2, productId);
                ps.setInt(3, quantity);
                ps.setTimestamp(4, now);
                ps.executeUpdate();
                try (ResultSet gk = ps.getGeneratedKeys()) {
                    if (gk.next()) {
                        CartItem item = new CartItem();
                        item.setId(gk.getLong(1));
                        item.setUserId(userId);
                        item.setProductId(productId);
                        item.setQuantity(quantity);
                        item.setCreatedAt(now);
                        return item;
                    }
                }
            } catch (SQLException e) {
                LOGGER.error("Error inserting cart item", e);
                throw new RuntimeException("Database error adding cart item", e);
            }
        }
        return null;
    }

    @Override
    public boolean updateQuantity(Long cartItemId, Long userId, int newQuantity) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_QTY)) {
            ps.setInt(1, newQuantity);
            ps.setLong(2, cartItemId);
            ps.setLong(3, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error("Error updating cart item ID {} quantity", cartItemId, e);
            throw new RuntimeException("Database error updating cart item quantity", e);
        }
    }

    @Override
    public boolean removeItem(Long cartItemId, Long userId) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_REMOVE)) {
            ps.setLong(1, cartItemId);
            ps.setLong(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error("Error removing cart item ID {}", cartItemId, e);
            throw new RuntimeException("Database error removing cart item", e);
        }
    }

    @Override
    public boolean clearCart(Connection conn, Long userId) {
        try (PreparedStatement ps = conn.prepareStatement(SQL_CLEAR_USER)) {
            ps.setLong(1, userId);
            return ps.executeUpdate() >= 0;
        } catch (SQLException e) {
            LOGGER.error("Error clearing cart for user ID {}", userId, e);
            throw new RuntimeException("Database error clearing user cart", e);
        }
    }

    @Override
    public boolean clearCart(Long userId) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            return clearCart(conn, userId);
        } catch (SQLException e) {
            LOGGER.error("Error acquiring connection to clear cart for user ID {}", userId, e);
            throw new RuntimeException("Database error clearing user cart", e);
        }
    }

    @Override
    public int countItemsByUser(Long userId) {
        if (userId == null) {
            return 0;
        }
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_COUNT_USER)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error counting cart items for user ID {}", userId, e);
            throw new RuntimeException("Database error counting cart items", e);
        }
        return 0;
    }
}
