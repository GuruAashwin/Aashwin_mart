package com.aashwin.aashwinmart.service;

import com.aashwin.aashwinmart.dao.DAOFactory;
import com.aashwin.aashwinmart.dao.ProductDAO;
import com.aashwin.aashwinmart.dto.ProductDTO;
import com.aashwin.aashwinmart.exception.AuthorizationException;
import com.aashwin.aashwinmart.exception.ResourceNotFoundException;
import com.aashwin.aashwinmart.model.Product;
import com.aashwin.aashwinmart.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of ProductService enforcing ownership boundaries, input validation, and business rules.
 */
public class ProductServiceImpl implements ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductDAO productDAO;

    public ProductServiceImpl() {
        this.productDAO = DAOFactory.getProductDAO();
    }

    public ProductServiceImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public ProductDTO createProduct(Long sellerId, String name, String description, BigDecimal price,
                                    Integer stockQty, String category, String imageUrl) {
        if (sellerId == null) {
            throw new IllegalArgumentException("Seller ID is required");
        }
        ValidationUtil.requireNonEmpty(name, "Product Name");
        ValidationUtil.requireNonEmpty(category, "Category");
        ValidationUtil.validatePrice(price);
        ValidationUtil.validateStockQty(stockQty);

        Product product = new Product();
        product.setSellerId(sellerId);
        product.setName(name.trim());
        product.setDescription(description != null ? description.trim() : "");
        product.setPrice(price);
        product.setStockQty(stockQty);
        product.setCategory(category.trim());
        product.setImageUrl(imageUrl != null ? imageUrl.trim() : "");
        product.setActive(true);

        Product saved = productDAO.create(product);
        LOGGER.info("Created product ID {} for seller ID {}", saved.getId(), sellerId);
        return getProductById(saved.getId());
    }

    @Override
    public ProductDTO updateProduct(Long sellerId, Long productId, String name, String description,
                                    BigDecimal price, Integer stockQty, String category, String imageUrl, Boolean active) {
        ValidationUtil.requireNonEmpty(name, "Product Name");
        ValidationUtil.requireNonEmpty(category, "Category");
        ValidationUtil.validatePrice(price);
        ValidationUtil.validateStockQty(stockQty);

        Product existing = productDAO.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        // Ownership enforcement
        if (!Objects.equals(existing.getSellerId(), sellerId)) {
            LOGGER.warn("Unauthorized attempt by seller {} to modify product {}", sellerId, productId);
            throw new AuthorizationException("You do not have permission to modify this product listing");
        }

        existing.setName(name.trim());
        existing.setDescription(description != null ? description.trim() : "");
        existing.setPrice(price);
        existing.setStockQty(stockQty);
        existing.setCategory(category.trim());
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            existing.setImageUrl(imageUrl.trim());
        }
        if (active != null) {
            existing.setActive(active);
        }

        productDAO.update(existing);
        LOGGER.info("Updated product ID {} by seller ID {}", productId, sellerId);
        return getProductById(productId);
    }

    @Override
    public boolean deleteProduct(Long sellerId, Long productId) {
        Product existing = productDAO.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        if (!Objects.equals(existing.getSellerId(), sellerId)) {
            LOGGER.warn("Unauthorized delete attempt by seller {} for product {}", sellerId, productId);
            throw new AuthorizationException("You do not have permission to delete this product");
        }

        LOGGER.info("Seller ID {} deleted product ID {}", sellerId, productId);
        return productDAO.delete(productId);
    }

    @Override
    public boolean adminDeleteProduct(Long productId) {
        LOGGER.info("Admin moderated/deleted product ID {}", productId);
        return productDAO.delete(productId);
    }

    @Override
    public boolean adminToggleActive(Long productId, boolean active) {
        Product existing = productDAO.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
        existing.setActive(active);
        return productDAO.update(existing);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return productDAO.findById(id)
                .map(ProductDTO::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
    }

    @Override
    public List<ProductDTO> getProductsWithFilters(String keyword, String category, String sort, int page, int pageSize) {
        int limit = pageSize > 0 ? pageSize : 12;
        int offset = Math.max((page - 1) * limit, 0);
        return productDAO.findAllWithFilters(keyword, category, sort, offset, limit).stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public int countProductsWithFilters(String keyword, String category) {
        return productDAO.countWithFilters(keyword, category);
    }

    @Override
    public List<ProductDTO> getSellerProducts(Long sellerId) {
        return productDAO.findBySellerId(sellerId).stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getFeaturedProducts(int limit) {
        return productDAO.findFeatured(limit).stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllCategories() {
        return productDAO.findAllCategories();
    }

    @Override
    public List<ProductDTO> getAllProductsForAdmin() {
        return productDAO.findAll().stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public long countAllProducts() {
        return productDAO.countAll();
    }
}
