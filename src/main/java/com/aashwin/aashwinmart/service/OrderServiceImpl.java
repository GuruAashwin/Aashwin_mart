package com.aashwin.aashwinmart.service;

import com.aashwin.aashwinmart.config.DatabaseConfig;
import com.aashwin.aashwinmart.dao.CartItemDAO;
import com.aashwin.aashwinmart.dao.DAOFactory;
import com.aashwin.aashwinmart.dao.OrderDAO;
import com.aashwin.aashwinmart.dao.OrderItemDAO;
import com.aashwin.aashwinmart.dao.ProductDAO;
import com.aashwin.aashwinmart.dto.CheckoutRequestDTO;
import com.aashwin.aashwinmart.dto.OrderDTO;
import com.aashwin.aashwinmart.exception.AppException;
import com.aashwin.aashwinmart.exception.AuthorizationException;
import com.aashwin.aashwinmart.exception.InsufficientStockException;
import com.aashwin.aashwinmart.exception.ResourceNotFoundException;
import com.aashwin.aashwinmart.exception.ValidationException;
import com.aashwin.aashwinmart.model.CartItem;
import com.aashwin.aashwinmart.model.Order;
import com.aashwin.aashwinmart.model.OrderItem;
import com.aashwin.aashwinmart.model.OrderStatus;
import com.aashwin.aashwinmart.model.Product;
import com.aashwin.aashwinmart.model.Role;
import com.aashwin.aashwinmart.service.strategy.MockCardPaymentStrategy;
import com.aashwin.aashwinmart.service.strategy.MockNetBankingPaymentStrategy;
import com.aashwin.aashwinmart.service.strategy.MockUpiPaymentStrategy;
import com.aashwin.aashwinmart.service.strategy.PaymentStrategy;
import com.aashwin.aashwinmart.util.DbUtil;
import com.aashwin.aashwinmart.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of OrderService ensuring transactional integrity, atomic stock deductions, and role enforcement.
 */
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderDAO orderDAO;
    private final OrderItemDAO orderItemDAO;
    private final CartItemDAO cartItemDAO;
    private final ProductDAO productDAO;

    public OrderServiceImpl() {
        this.orderDAO = DAOFactory.getOrderDAO();
        this.orderItemDAO = DAOFactory.getOrderItemDAO();
        this.cartItemDAO = DAOFactory.getCartItemDAO();
        this.productDAO = DAOFactory.getProductDAO();
    }

    public OrderServiceImpl(OrderDAO orderDAO, OrderItemDAO orderItemDAO, CartItemDAO cartItemDAO, ProductDAO productDAO) {
        this.orderDAO = orderDAO;
        this.orderItemDAO = orderItemDAO;
        this.cartItemDAO = cartItemDAO;
        this.productDAO = productDAO;
    }

    @Override
    public OrderDTO checkout(Long buyerId, CheckoutRequestDTO request) {
        if (buyerId == null) {
            throw new IllegalArgumentException("Buyer ID is required");
        }
        if (request == null) {
            throw new ValidationException("Checkout request cannot be null");
        }
        ValidationUtil.requireNonEmpty(request.getFullName(), "Full Name");
        ValidationUtil.requireNonEmpty(request.getAddressLine(), "Address");
        ValidationUtil.requireNonEmpty(request.getCity(), "City");
        ValidationUtil.requireNonEmpty(request.getState(), "State");
        ValidationUtil.requireNonEmpty(request.getPostalCode(), "Postal Code");
        ValidationUtil.requireNonEmpty(request.getPhone(), "Phone Number");

        List<CartItem> cartItems = cartItemDAO.findByUserId(buyerId);
        if (cartItems.isEmpty()) {
            throw new ValidationException("Your shopping cart is empty");
        }

        // Calculate total amount & validate inventory
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            Product product = productDAO.findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product ID " + item.getProductId() + " is no longer available"));

            if (!Boolean.TRUE.equals(product.getActive())) {
                throw new ValidationException("Product '" + product.getName() + "' is no longer active");
            }
            if (product.getStockQty() < item.getQuantity()) {
                throw new InsufficientStockException("Insufficient stock for '" + product.getName() +
                        "'. Requested: " + item.getQuantity() + ", Available: " + product.getStockQty());
            }
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        // Execute Mock Payment Strategy
        PaymentStrategy paymentStrategy;
        String paymentDetails = "";
        String method = request.getPaymentMethod() != null ? request.getPaymentMethod().toUpperCase() : "CARD";
        if ("UPI".equals(method)) {
            paymentStrategy = new MockUpiPaymentStrategy();
            paymentDetails = request.getUpiId();
        } else if ("NETBANKING".equals(method)) {
            paymentStrategy = new MockNetBankingPaymentStrategy();
        } else {
            paymentStrategy = new MockCardPaymentStrategy();
            paymentDetails = request.getCardNumber();
        }

        PaymentStrategy.PaymentResult paymentResult = paymentStrategy.processPayment(totalAmount, paymentDetails);
        if (!paymentResult.isSuccess()) {
            throw new ValidationException("Payment authorization failed: " + paymentResult.getMessage());
        }
        LOGGER.info("Mock payment successful: {}", paymentResult.getMessage());

        // Transactional Execution: Create Order, Batch Insert Items, Deduct Stock, Clear Cart
        Connection conn = null;
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false); // Begin Transaction

            Order order = new Order();
            order.setBuyerId(buyerId);
            order.setStatus(OrderStatus.CONFIRMED);
            order.setTotalAmount(totalAmount);
            order.setShippingAddress(request.getFormattedAddress());
            order.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            Order createdOrder = orderDAO.create(conn, order);
            List<OrderItem> orderItems = new ArrayList<>();

            for (CartItem cartItem : cartItems) {
                Product product = productDAO.findById(cartItem.getProductId()).orElseThrow();

                // Deduct stock within transaction
                boolean deducted = productDAO.updateStock(conn, product.getId(), cartItem.getQuantity());
                if (!deducted) {
                    throw new InsufficientStockException("Stock changed during checkout for product: " + product.getName());
                }

                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(createdOrder.getId());
                orderItem.setProductId(product.getId());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setUnitPrice(product.getPrice());
                orderItems.add(orderItem);
            }

            // Batch insert line items
            orderItemDAO.batchInsert(conn, orderItems);

            // Clear buyer's cart within transaction
            cartItemDAO.clearCart(conn, buyerId);

            conn.commit(); // Commit Transaction
            LOGGER.info("Order ID {} placed successfully for buyer ID {} with total ${}", createdOrder.getId(), buyerId, totalAmount);

            return getOrderById(createdOrder.getId(), buyerId, Role.BUYER);
        } catch (Exception e) {
            DbUtil.rollbackQuietly(conn);
            LOGGER.error("Checkout transaction rolled back due to error", e);
            if (e instanceof AppException) {
                throw (AppException) e;
            }
            throw new RuntimeException("Checkout failed, transaction was rolled back", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    @Override
    public OrderDTO getOrderById(Long orderId, Long requestingUserId, Role role) {
        Order order = orderDAO.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        // Authorization rules: Admin can view any; Buyer only their own; Seller only if order has their products
        if (Role.ADMIN.equals(role)) {
            return OrderDTO.fromEntity(order);
        } else if (Role.BUYER.equals(role)) {
            if (!Objects.equals(order.getBuyerId(), requestingUserId)) {
                throw new AuthorizationException("You do not have permission to view this order");
            }
            return OrderDTO.fromEntity(order);
        } else if (Role.SELLER.equals(role)) {
            List<OrderItem> sellerItems = orderItemDAO.findByOrderIdAndSellerId(orderId, requestingUserId);
            if (sellerItems.isEmpty()) {
                throw new AuthorizationException("You do not have permission to view this order");
            }
            order.setItems(sellerItems);
            return OrderDTO.fromEntity(order);
        }

        throw new AuthorizationException("Unauthorized access to order details");
    }

    @Override
    public List<OrderDTO> getBuyerOrders(Long buyerId) {
        return orderDAO.findByBuyerId(buyerId).stream()
                .map(OrderDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getSellerIncomingOrders(Long sellerId) {
        return orderDAO.findBySellerId(sellerId).stream()
                .map(OrderDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getAllOrdersForAdmin() {
        return orderDAO.findAll().stream()
                .map(OrderDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateOrderStatus(Long orderId, OrderStatus newStatus, Long requestingUserId, Role role) {
        if (orderId == null || newStatus == null) {
            return false;
        }

        Order order = orderDAO.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        // Only Admin or Seller with products in this order can update status
        if (!Role.ADMIN.equals(role) && !Role.SELLER.equals(role)) {
            throw new AuthorizationException("Only Sellers or Administrators can update order fulfillment status");
        }

        if (Role.SELLER.equals(role)) {
            List<OrderItem> sellerItems = orderItemDAO.findByOrderIdAndSellerId(orderId, requestingUserId);
            if (sellerItems.isEmpty()) {
                throw new AuthorizationException("You do not sell products in this order");
            }
        }

        LOGGER.info("Order ID {} status updated to {} by user ID {} [{}]", orderId, newStatus, requestingUserId, role);
        return orderDAO.updateStatus(orderId, newStatus);
    }

    @Override
    public long countAllOrders() {
        return orderDAO.countAll();
    }

    @Override
    public long countOrdersByStatus(OrderStatus status) {
        return orderDAO.countByStatus(status);
    }

    @Override
    public BigDecimal getTotalRevenue() {
        return orderDAO.calculateTotalRevenue();
    }

    @Override
    public BigDecimal getSellerRevenue(Long sellerId) {
        return orderDAO.calculateSellerRevenue(sellerId);
    }
}
