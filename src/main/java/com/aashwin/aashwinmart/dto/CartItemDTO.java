package com.aashwin.aashwinmart.dto;

import com.aashwin.aashwinmart.model.CartItem;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Data Transfer Object for Cart items.
 */
public class CartItemDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long productId;
    private String productName;
    private String productImageUrl;
    private BigDecimal unitPrice;
    private Integer quantity;
    private Integer stockQty;
    private BigDecimal itemTotal;
    private Long sellerId;
    private String sellerName;

    public CartItemDTO() {
    }

    public static CartItemDTO fromEntity(CartItem entity) {
        if (entity == null) {
            return null;
        }
        CartItemDTO dto = new CartItemDTO();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProductId());
        dto.setProductName(entity.getProductName());
        dto.setProductImageUrl(entity.getProductImageUrl());
        dto.setUnitPrice(entity.getProductPrice());
        dto.setQuantity(entity.getQuantity());
        dto.setStockQty(entity.getProductStockQty());
        dto.setItemTotal(entity.getItemTotal());
        dto.setSellerId(entity.getSellerId());
        dto.setSellerName(entity.getSellerName());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getStockQty() {
        return stockQty;
    }

    public void setStockQty(Integer stockQty) {
        this.stockQty = stockQty;
    }

    public BigDecimal getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(BigDecimal itemTotal) {
        this.itemTotal = itemTotal;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }
}
