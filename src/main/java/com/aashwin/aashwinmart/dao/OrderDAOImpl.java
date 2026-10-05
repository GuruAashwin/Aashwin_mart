package com.aashwin.aashwinmart.dao;

import com.aashwin.aashwinmart.config.DatabaseConfig;
import com.aashwin.aashwinmart.model.Order;
import com.aashwin.aashwinmart.model.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
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
 * JDBC implementation of OrderDAO.
 */
public class OrderDAOImpl implements OrderDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDAOImpl.class);

    private static final String BASE_SELECT =
            "SELECT o.id, o.buyer_id, o.status, o.total_amount, o.shipping_address, o.created_at, " +
            "       u.name AS buyer_name, u.email AS buyer_email " +
            "FROM orders o " +
            "JOIN users u ON o.buyer_id = u.id ";

    private static final String SQL_INSERT =
            "INSERT INTO orders (buyer_id, status, total_amount, shipping_address, created_at) VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE_STATUS =
            "UPDATE orders SET status = ? WHERE id = ?";

    private static final String SQL_COUNT_ALL =
            "SELECT COUNT(*) FROM orders";

    private static final String SQL_COUNT_BY_STATUS =
            "SELECT COUNT(*) FROM orders WHERE status = ?";

    private static final String SQL_TOTAL_REVENUE =
            "SELECT COALESCE(SUM(total_amount), 0.00) FROM orders WHERE status != 'CANCELLED'";

    private static final String SQL_SELLER_REVENUE =
            "SELECT COALESCE(SUM(oi.quantity * oi.unit_price), 0.00) " +
            "FROM order_items oi " +
            "JOIN orders o ON oi.order_id = o.id " +
            "JOIN products p ON oi.product_id = p.id " +
            "WHERE p.seller_id = ? AND o.status != 'CANCELLED'";

    private final OrderItemDAO orderItemDAO;

    public OrderDAOImpl() {
        this.orderItemDAO = new OrderItemDAOImpl();
    }

    public OrderDAOImpl(OrderItemDAO orderItemDAO) {
        this.orderItemDAO = orderItemDAO;
    }

    @Override
    public Order create(Connection conn, Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        Timestamp now = order.getCreatedAt() != null ? order.getCreatedAt() : new Timestamp(System.currentTimeMillis());
        try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, order.getBuyerId());
            ps.setString(2, order.getStatus() != null ? order.getStatus().name() : OrderStatus.PENDING.name());
            ps.setBigDecimal(3, order.getTotalAmount());
            ps.setString(4, order.getShippingAddress());
            ps.setTimestamp(5, now);

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) {
                    order.setId(gk.getLong(1));
                    order.setCreatedAt(now);
                }
            }
            return order;
        } catch (SQLException e) {
            LOGGER.error("Error creating order for buyer ID {}", order.getBuyerId(), e);
            throw new RuntimeException("Database error creating order", e);
        }
    }

    @Override
    public Optional<Order> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        String sql = BASE_SELECT + "WHERE o.id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    order.setItems(orderItemDAO.findByOrderId(order.getId()));
                    return Optional.of(order);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding order by ID {}", id, e);
            throw new RuntimeException("Database error finding order", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Order> findByBuyerId(Long buyerId) {
        List<Order> list = new ArrayList<>();
        if (buyerId == null) {
            return list;
        }
        String sql = BASE_SELECT + "WHERE o.buyer_id = ? ORDER BY o.created_at DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, buyerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    order.setItems(orderItemDAO.findByOrderId(order.getId()));
                    list.add(order);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding orders for buyer ID {}", buyerId, e);
            throw new RuntimeException("Database error finding buyer orders", e);
        }
        return list;
    }

    @Override
    public List<Order> findBySellerId(Long sellerId) {
        List<Order> list = new ArrayList<>();
        if (sellerId == null) {
            return list;
        }
        String sql = "SELECT DISTINCT o.id, o.buyer_id, o.status, o.total_amount, o.shipping_address, o.created_at, " +
                     "       u.name AS buyer_name, u.email AS buyer_email " +
                     "FROM orders o " +
                     "JOIN users u ON o.buyer_id = u.id " +
                     "JOIN order_items oi ON o.id = oi.order_id " +
                     "JOIN products p ON oi.product_id = p.id " +
                     "WHERE p.seller_id = ? " +
                     "ORDER BY o.created_at DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    order.setItems(orderItemDAO.findByOrderIdAndSellerId(order.getId(), sellerId));
                    list.add(order);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding incoming orders for seller ID {}", sellerId, e);
            throw new RuntimeException("Database error finding seller orders", e);
        }
        return list;
    }

    @Override
    public List<Order> findAll() {
        List<Order> list = new ArrayList<>();
        String sql = BASE_SELECT + "ORDER BY o.created_at DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                order.setItems(orderItemDAO.findByOrderId(order.getId()));
                list.add(order);
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding all orders for admin", e);
            throw new RuntimeException("Database error finding all orders", e);
        }
        return list;
    }

    @Override
    public boolean updateStatus(Long orderId, OrderStatus status) {
        if (orderId == null || status == null) {
            return false;
        }
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_STATUS)) {
            ps.setString(1, status.name());
            ps.setLong(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error("Error updating status for order ID {}", orderId, e);
            throw new RuntimeException("Database error updating order status", e);
        }
    }

    @Override
    public long countAll() {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_COUNT_ALL);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            LOGGER.error("Error counting all orders", e);
            throw new RuntimeException("Database error counting orders", e);
        }
        return 0;
    }

    @Override
    public long countByStatus(OrderStatus status) {
        if (status == null) {
            return 0;
        }
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_COUNT_BY_STATUS)) {
            ps.setString(1, status.name());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error counting orders by status {}", status, e);
            throw new RuntimeException("Database error counting orders by status", e);
        }
        return 0;
    }

    @Override
    public BigDecimal calculateTotalRevenue() {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_TOTAL_REVENUE);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
        } catch (SQLException e) {
            LOGGER.error("Error calculating total revenue", e);
            throw new RuntimeException("Database error calculating revenue", e);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal calculateSellerRevenue(Long sellerId) {
        if (sellerId == null) {
            return BigDecimal.ZERO;
        }
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELLER_REVENUE)) {
            ps.setLong(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error calculating seller revenue for seller ID {}", sellerId, e);
            throw new RuntimeException("Database error calculating seller revenue", e);
        }
        return BigDecimal.ZERO;
    }

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getLong("id"));
        o.setBuyerId(rs.getLong("buyer_id"));
        o.setStatus(OrderStatus.fromString(rs.getString("status")));
        o.setTotalAmount(rs.getBigDecimal("total_amount"));
        o.setShippingAddress(rs.getString("shipping_address"));
        o.setCreatedAt(rs.getTimestamp("created_at"));
        o.setBuyerName(rs.getString("buyer_name"));
        o.setBuyerEmail(rs.getString("buyer_email"));
        return o;
    }
}
