package com.aashwin.aashwinmart.dao;

import com.aashwin.aashwinmart.config.DatabaseConfig;
import com.aashwin.aashwinmart.model.OrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of OrderItemDAO.
 */
public class OrderItemDAOImpl implements OrderItemDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderItemDAOImpl.class);

    private static final String SQL_INSERT =
            "INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";

    private static final String SQL_FIND_BY_ORDER =
            "SELECT oi.id, oi.order_id, oi.product_id, oi.quantity, oi.unit_price, " +
            "       p.name AS product_name, p.image_url AS product_image_url, " +
            "       p.seller_id, u.name AS seller_name " +
            "FROM order_items oi " +
            "JOIN products p ON oi.product_id = p.id " +
            "JOIN users u ON p.seller_id = u.id " +
            "WHERE oi.order_id = ?";

    private static final String SQL_FIND_BY_ORDER_AND_SELLER =
            "SELECT oi.id, oi.order_id, oi.product_id, oi.quantity, oi.unit_price, " +
            "       p.name AS product_name, p.image_url AS product_image_url, " +
            "       p.seller_id, u.name AS seller_name " +
            "FROM order_items oi " +
            "JOIN products p ON oi.product_id = p.id " +
            "JOIN users u ON p.seller_id = u.id " +
            "WHERE oi.order_id = ? AND p.seller_id = ?";

    @Override
    public OrderItem create(Connection conn, OrderItem item) {
        try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, item.getOrderId());
            ps.setLong(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setBigDecimal(4, item.getUnitPrice());

            ps.executeUpdate();
            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) {
                    item.setId(gk.getLong(1));
                }
            }
            return item;
        } catch (SQLException e) {
            LOGGER.error("Error creating order item for order ID {}", item.getOrderId(), e);
            throw new RuntimeException("Database error creating order item", e);
        }
    }

    @Override
    public void batchInsert(Connection conn, List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            return;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            for (OrderItem item : items) {
                ps.setLong(1, item.getOrderId());
                ps.setLong(2, item.getProductId());
                ps.setInt(3, item.getQuantity());
                ps.setBigDecimal(4, item.getUnitPrice());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            LOGGER.error("Error batch inserting order items", e);
            throw new RuntimeException("Database error batch inserting order items", e);
        }
    }

    @Override
    public List<OrderItem> findByOrderId(Long orderId) {
        List<OrderItem> list = new ArrayList<>();
        if (orderId == null) {
            return list;
        }
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ORDER)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToOrderItem(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding order items for order ID {}", orderId, e);
            throw new RuntimeException("Database error finding order items", e);
        }
        return list;
    }

    @Override
    public List<OrderItem> findByOrderIdAndSellerId(Long orderId, Long sellerId) {
        List<OrderItem> list = new ArrayList<>();
        if (orderId == null || sellerId == null) {
            return list;
        }
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ORDER_AND_SELLER)) {
            ps.setLong(1, orderId);
            ps.setLong(2, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToOrderItem(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding order items for order {} and seller {}", orderId, sellerId, e);
            throw new RuntimeException("Database error finding seller order items", e);
        }
        return list;
    }

    private OrderItem mapResultSetToOrderItem(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();
        item.setId(rs.getLong("id"));
        item.setOrderId(rs.getLong("order_id"));
        item.setProductId(rs.getLong("product_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnitPrice(rs.getBigDecimal("unit_price"));
        item.setProductName(rs.getString("product_name"));
        item.setProductImageUrl(rs.getString("product_image_url"));
        item.setSellerId(rs.getLong("seller_id"));
        item.setSellerName(rs.getString("seller_name"));
        return item;
    }
}
