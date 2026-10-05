package com.aashwin.aashwinmart.service;

import com.aashwin.aashwinmart.dto.ProductDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for Product Management (F2), Catalog Browsing & Search (F3), and Product Moderation (F7).
 */
public interface ProductService {

    ProductDTO createProduct(Long sellerId, String name, String description, BigDecimal price,
                             Integer stockQty, String category, String imageUrl);

    ProductDTO updateProduct(Long sellerId, Long productId, String name, String description,
                             BigDecimal price, Integer stockQty, String category, String imageUrl, Boolean active);

    boolean deleteProduct(Long sellerId, Long productId);

    boolean adminDeleteProduct(Long productId);

    boolean adminToggleActive(Long productId, boolean active);

    ProductDTO getProductById(Long id);

    List<ProductDTO> getProductsWithFilters(String keyword, String category, String sort, int page, int pageSize);

    int countProductsWithFilters(String keyword, String category);

    List<ProductDTO> getSellerProducts(Long sellerId);

    List<ProductDTO> getFeaturedProducts(int limit);

    List<String> getAllCategories();

    List<ProductDTO> getAllProductsForAdmin();

    long countAllProducts();
}
