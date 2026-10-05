package com.aashwin.aashwinmart.dao;

import com.aashwin.aashwinmart.model.Product;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Product listings and catalog management.
 */
public interface ProductDAO {

    Product create(Product product);

    boolean update(Product product);

    boolean delete(Long id);

    Optional<Product> findById(Long id);

    List<Product> findAllWithFilters(String keyword, String category, String sort, int offset, int limit);

    int countWithFilters(String keyword, String category);

    List<Product> findBySellerId(Long sellerId);

    List<Product> findFeatured(int limit);

    List<String> findAllCategories();

    boolean updateStock(Connection conn, Long productId, int quantityToDeduct);

    long countAll();

    List<Product> findAll();
}
