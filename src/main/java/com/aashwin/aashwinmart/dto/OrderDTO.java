package com.aashwin.aashwinmart.dto;

import com.aashwin.aashwinmart.model.Order;
import com.aashwin.aashwinmart.model.OrderStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Data Transfer Object for Orders.
 */
public class OrderDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long buyerId;
    private String buyerName;
    private String buyerEmail;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private Timestamp createdAt;
    private List<OrderItemDTO> items = new ArrayList<>();

    public OrderDTO() {
    }

    private OrderDTO(Builder builder) {
        this.id = builder.id;
        this.buyerId = builder.buyerId;
        this.buyerName = builder.buyerName;
        this.buyerEmail = builder.buyerEmail;
        this.status = builder.status;
        this.totalAmount = builder.totalAmount;
        this.shippingAddress = builder.shippingAddress;
        this.createdAt = builder.createdAt;
        this.items = builder.items;
    }

    public static OrderDTO fromEntity(Order order) {
        if (order == null) {
            return null;
        }
        List<OrderItemDTO> itemDTOs = new ArrayList<>();
        if (order.getItems() != null) {
            itemDTOs = order.getItems().stream()
                    .map(OrderItemDTO::fromEntity)
                    .collect(Collectors.toList());
        }

        return new Builder()
                .id(order.getId())
                .buyerId(order.getBuyerId())
                .buyerName(order.getBuyerName())
                .buyerEmail(order.getBuyerEmail())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .shippingAddress(order.getShippingAddress())
                .createdAt(order.getCreatedAt())
                .items(itemDTOs)
                .build();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }

    public static class Builder {
        private Long id;
        private Long buyerId;
        private String buyerName;
        private String buyerEmail;
        private OrderStatus status = OrderStatus.PENDING;
        private BigDecimal totalAmount = BigDecimal.ZERO;
        private String shippingAddress;
        private Timestamp createdAt;
        private List<OrderItemDTO> items = new ArrayList<>();

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder buyerId(Long buyerId) {
            this.buyerId = buyerId;
            return this;
        }

        public Builder buyerName(String buyerName) {
            this.buyerName = buyerName;
            return this;
        }

        public Builder buyerEmail(String buyerEmail) {
            this.buyerEmail = buyerEmail;
            return this;
        }

        public Builder status(OrderStatus status) {
            this.status = status;
            return this;
        }

        public Builder totalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Builder shippingAddress(String shippingAddress) {
            this.shippingAddress = shippingAddress;
            return this;
        }

        public Builder createdAt(Timestamp createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder items(List<OrderItemDTO> items) {
            this.items = items;
            return this;
        }

        public OrderDTO build() {
            return new OrderDTO(this);
        }
    }
}
