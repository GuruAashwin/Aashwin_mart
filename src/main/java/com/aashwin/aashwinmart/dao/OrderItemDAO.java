package com.aashwin.aashwinmart.dao;

import com.aashwin.aashwinmart.model.OrderItem;

import java.sql.Connection;
import java.util.List;

/**
 * Data Access Object interface for Order Line Items.
 */
public interface OrderItemDAO {

    OrderItem create(Connection conn, OrderItem item);

    void batchInsert(Connection conn, List<OrderItem> items);

    List<OrderItem> findByOrderId(Long orderId);

    List<OrderItem> findByOrderIdAndSellerId(Long orderId, Long sellerId);
}
