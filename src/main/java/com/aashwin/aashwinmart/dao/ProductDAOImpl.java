package com.aashwin.aashwinmart.dao;

import com.aashwin.aashwinmart.config.DatabaseConfig;
import com.aashwin.aashwinmart.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * JDBC implementation of ProductDAO.
 */
public class ProductDAOImpl implements ProductDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductDAOImpl.class);

    private static final String BASE_SELECT =
            "SELECT p.id, p.seller_id, p.name, p.description, p.price, p.stock_qty, p.category, p.image_url, p.is_active, p.created_at, " +
            "       u.name AS seller_name, " +
            "       COALESCE(AVG(r.rating), 0.0) AS avg_rating, " +
            "       COUNT(r.id) AS review_count " +
            "FROM products p " +
            "JOIN users u ON p.seller_id = u.id " +
            "LEFT JOIN reviews r ON p.id = r.product_id ";

    private static final String GROUP_BY =
            " GROUP BY p.id, p.seller_id, p.name, p.description, p.price, p.stock_qty, p.category, p.image_url, p.is_active, p.created_at, u.name ";

    @Override
    public Product create(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        String sql = "INSERT INTO products (seller_id, name, description, price, stock_qty, category, image_url, is_active, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Timestamp now = product.getCreatedAt() != null ? product.getCreatedAt() : new Timestamp(System.currentTimeMillis());

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, product.getSellerId());
            ps.setString(2, product.getName().trim());
            ps.setString(3, product.getDescription() != null ? product.getDescription().trim() : "");
            ps.setBigDecimal(4, product.getPrice());
            ps.setInt(5, product.getStockQty() != null ? product.getStockQty() : 0);
            ps.setString(6, product.getCategory().trim());
            ps.setString(7, product.getImageUrl() != null ? product.getImageUrl().trim() : "");
            ps.setBoolean(8, product.getActive() != null ? product.getActive() : true);
            ps.setTimestamp(9, now);

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Creating product failed, no rows affected.");
            }

            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) {
                    product.setId(gk.getLong(1));
                    product.setCreatedAt(now);
                }
            }
            return product;
        } catch (SQLException e) {
            LOGGER.error("Error creating product: {}", product.getName(), e);
            throw new RuntimeException("Database error creating product", e);
        }
    }

    @Override
    public boolean update(Product product) {
        if (product == null || product.getId() == null) {
            return false;
        }
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, stock_qty = ?, category = ?, image_url = ?, is_active = ? " +
                     "WHERE id = ? AND seller_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getName().trim());
            ps.setString(2, product.getDescription() != null ? product.getDescription().trim() : "");
            ps.setBigDecimal(3, product.getPrice());
            ps.setInt(4, product.getStockQty());
            ps.setString(5, product.getCategory().trim());
            ps.setString(6, product.getImageUrl() != null ? product.getImageUrl().trim() : "");
            ps.setBoolean(7, product.getActive());
            ps.setLong(8, product.getId());
            ps.setLong(9, product.getSellerId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error("Error updating product ID: {}", product.getId(), e);
            throw new RuntimeException("Database error updating product", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        if (id == null) {
            return false;
        }
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error("Error deleting product ID: {}", id, e);
            throw new RuntimeException("Database error deleting product", e);
        }
    }

    @Override
    public Optional<Product> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        String sql = BASE_SELECT + "WHERE p.id = ? " + GROUP_BY;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding product by ID: {}", id, e);
            throw new RuntimeException("Database error finding product", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Product> findAllWithFilters(String keyword, String category, String sort, int offset, int limit) {
        List<Product> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder(BASE_SELECT).append("WHERE p.is_active = TRUE ");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (LOWER(p.name) LIKE ? OR LOWER(p.description) LIKE ?) ");
            String kwParam = "%" + keyword.trim().toLowerCase() + "%";
            params.add(kwParam);
            params.add(kwParam);
        }

        if (category != null && !category.trim().isEmpty() && !"ALL".equalsIgnoreCase(category.trim())) {
            sql.append("AND LOWER(p.category) = LOWER(?) ");
            params.add(category.trim());
        }

        sql.append(GROUP_BY);

        // Sorting
        if ("price_asc".equalsIgnoreCase(sort)) {
            sql.append("ORDER BY p.price ASC ");
        } else if ("price_desc".equalsIgnoreCase(sort)) {
            sql.append("ORDER BY p.price DESC ");
        } else if ("rating".equalsIgnoreCase(sort)) {
            sql.append("ORDER BY avg_rating DESC ");
        } else {
            sql.append("ORDER BY p.created_at DESC ");
        }

        sql.append("LIMIT ? OFFSET ?");
        params.add(limit > 0 ? limit : 12);
        params.add(Math.max(offset, 0));

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding products with filters", e);
            throw new RuntimeException("Database error searching products", e);
        }
        return products;
    }

    @Override
    public int countWithFilters(String keyword, String category) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM products p WHERE p.is_active = TRUE ");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (LOWER(p.name) LIKE ? OR LOWER(p.description) LIKE ?) ");
            String kwParam = "%" + keyword.trim().toLowerCase() + "%";
            params.add(kwParam);
            params.add(kwParam);
        }

        if (category != null && !category.trim().isEmpty() && !"ALL".equalsIgnoreCase(category.trim())) {
            sql.append("AND LOWER(p.category) = LOWER(?) ");
            params.add(category.trim());
        }

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error counting products with filters", e);
            throw new RuntimeException("Database error counting products", e);
        }
        return 0;
    }

    @Override
    public List<Product> findBySellerId(Long sellerId) {
        List<Product> products = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE p.seller_id = ? " + GROUP_BY + "ORDER BY p.created_at DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding products by seller ID: {}", sellerId, e);
            throw new RuntimeException("Database error finding seller products", e);
        }
        return products;
    }

    @Override
    public List<Product> findFeatured(int limit) {
        List<Product> products = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE p.is_active = TRUE " + GROUP_BY + "ORDER BY p.created_at DESC LIMIT ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit > 0 ? limit : 8);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding featured products", e);
            throw new RuntimeException("Database error finding featured products", e);
        }
        return products;
    }

    @Override
    public List<String> findAllCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM products WHERE is_active = TRUE ORDER BY category ASC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding all categories", e);
            throw new RuntimeException("Database error finding categories", e);
        }
        return categories;
    }

    @Override
    public boolean updateStock(Connection conn, Long productId, int quantityToDeduct) {
        String sql = "UPDATE products SET stock_qty = stock_qty - ? WHERE id = ? AND stock_qty >= ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantityToDeduct);
            ps.setLong(2, productId);
            ps.setInt(3, quantityToDeduct);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error("Error deducting stock for product ID: {}", productId, e);
            throw new RuntimeException("Database error updating stock", e);
        }
    }

    @Override
    public long countAll() {
        String sql = "SELECT COUNT(*) FROM products";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            LOGGER.error("Error counting all products", e);
            throw new RuntimeException("Database error counting all products", e);
        }
        return 0;
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String sql = BASE_SELECT + GROUP_BY + "ORDER BY p.created_at DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding all products for admin", e);
            throw new RuntimeException("Database error finding all products", e);
        }
        return products;
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getLong("id"));
        p.setSellerId(rs.getLong("seller_id"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setStockQty(rs.getInt("stock_qty"));
        p.setCategory(rs.getString("category"));
        p.setImageUrl(rs.getString("image_url"));
        p.setActive(rs.getBoolean("is_active"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setSellerName(rs.getString("seller_name"));
        p.setAverageRating(rs.getDouble("avg_rating"));
        p.setReviewCount(rs.getInt("review_count"));
        return p;
    }
}
