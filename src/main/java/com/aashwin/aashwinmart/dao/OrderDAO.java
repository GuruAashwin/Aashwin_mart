package com.aashwin.aashwinmart.dao;

import com.aashwin.aashwinmart.model.Order;
import com.aashwin.aashwinmart.model.OrderStatus;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Orders.
 */
public interface OrderDAO {

    Order create(Connection conn, Order order);

    Optional<Order> findById(Long id);

    List<Order> findByBuyerId(Long buyerId);

    List<Order> findBySellerId(Long sellerId);

    List<Order> findAll();

    boolean updateStatus(Long orderId, OrderStatus status);

    long countAll();

    long countByStatus(OrderStatus status);

    BigDecimal calculateTotalRevenue();

    BigDecimal calculateSellerRevenue(Long sellerId);
}
