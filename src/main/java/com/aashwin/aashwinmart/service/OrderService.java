package com.aashwin.aashwinmart.service;

import com.aashwin.aashwinmart.dto.CheckoutRequestDTO;
import com.aashwin.aashwinmart.dto.OrderDTO;
import com.aashwin.aashwinmart.model.OrderStatus;
import com.aashwin.aashwinmart.model.Role;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for Order Processing (F5), Order History & Incoming Orders (F6),
 * Order Status Workflow (O2), and Seller Sales Dashboard (O3).
 */
public interface OrderService {

    OrderDTO checkout(Long buyerId, CheckoutRequestDTO request);

    OrderDTO getOrderById(Long orderId, Long requestingUserId, Role role);

    List<OrderDTO> getBuyerOrders(Long buyerId);

    List<OrderDTO> getSellerIncomingOrders(Long sellerId);

    List<OrderDTO> getAllOrdersForAdmin();

    boolean updateOrderStatus(Long orderId, OrderStatus newStatus, Long requestingUserId, Role role);

    long countAllOrders();

    long countOrdersByStatus(OrderStatus status);

    BigDecimal getTotalRevenue();

    BigDecimal getSellerRevenue(Long sellerId);
}
