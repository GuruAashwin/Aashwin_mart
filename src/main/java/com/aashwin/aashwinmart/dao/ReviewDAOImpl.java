package com.aashwin.aashwinmart.dao;

import com.aashwin.aashwinmart.config.DatabaseConfig;
import com.aashwin.aashwinmart.model.Review;
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

/**
 * JDBC implementation of ReviewDAO.
 */
public class ReviewDAOImpl implements ReviewDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewDAOImpl.class);

    private static final String SQL_INSERT =
            "INSERT INTO reviews (product_id, user_id, rating, comment, created_at) VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_FIND_BY_PRODUCT =
            "SELECT r.id, r.product_id, r.user_id, r.rating, r.comment, r.created_at, " +
            "       u.name AS user_name, p.name AS product_name " +
            "FROM reviews r " +
            "JOIN users u ON r.user_id = u.id " +
            "JOIN products p ON r.product_id = p.id " +
            "WHERE r.product_id = ? " +
            "ORDER BY r.created_at DESC";

    private static final String SQL_CHECK_PURCHASED =
            "SELECT COUNT(oi.id) " +
            "FROM order_items oi " +
            "JOIN orders o ON oi.order_id = o.id " +
            "WHERE o.buyer_id = ? AND oi.product_id = ? AND o.status IN ('DELIVERED', 'SHIPPED', 'CONFIRMED')";

    private static final String SQL_CHECK_REVIEWED =
            "SELECT COUNT(*) FROM reviews WHERE user_id = ? AND product_id = ?";

    private static final String SQL_AVG_RATING =
            "SELECT COALESCE(AVG(rating), 0.0), COUNT(id) FROM reviews WHERE product_id = ?";

    @Override
    public Review create(Review review) {
        if (review == null) {
            throw new IllegalArgumentException("Review cannot be null");
        }
        Timestamp now = review.getCreatedAt() != null ? review.getCreatedAt() : new Timestamp(System.currentTimeMillis());
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, review.getProductId());
            ps.setLong(2, review.getUserId());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getComment() != null ? review.getComment().trim() : "");
            ps.setTimestamp(5, now);

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Creating review failed, no rows affected.");
            }

            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) {
                    review.setId(gk.getLong(1));
                    review.setCreatedAt(now);
                }
            }
            return review;
        } catch (SQLException e) {
            LOGGER.error("Error creating review for product ID {}", review.getProductId(), e);
            throw new RuntimeException("Database error creating review", e);
        }
    }

    @Override
    public List<Review> findByProductId(Long productId) {
        List<Review> list = new ArrayList<>();
        if (productId == null) {
            return list;
        }
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_PRODUCT)) {
            ps.setLong(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Review r = new Review();
                    r.setId(rs.getLong("id"));
                    r.setProductId(rs.getLong("product_id"));
                    r.setUserId(rs.getLong("user_id"));
                    r.setRating(rs.getInt("rating"));
                    r.setComment(rs.getString("comment"));
                    r.setCreatedAt(rs.getTimestamp("created_at"));
                    r.setUserName(rs.getString("user_name"));
                    r.setProductName(rs.getString("product_name"));
                    list.add(r);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding reviews for product ID {}", productId, e);
            throw new RuntimeException("Database error finding reviews", e);
        }
        return list;
    }

    @Override
    public boolean hasUserPurchasedProduct(Long userId, Long productId) {
        if (userId == null || productId == null) {
            return false;
        }
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_CHECK_PURCHASED)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error checking purchase eligibility for user {} and product {}", userId, productId, e);
            throw new RuntimeException("Database error checking purchase eligibility", e);
        }
        return false;
    }

    @Override
    public boolean hasUserReviewedProduct(Long userId, Long productId) {
        if (userId == null || productId == null) {
            return false;
        }
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_CHECK_REVIEWED)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error checking review presence for user {} and product {}", userId, productId, e);
            throw new RuntimeException("Database error checking review existence", e);
        }
        return false;
    }

    @Override
    public double getAverageRating(Long productId) {
        if (productId == null) {
            return 0.0;
        }
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_AVG_RATING)) {
            ps.setLong(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error calculating average rating for product ID {}", productId, e);
            throw new RuntimeException("Database error calculating average rating", e);
        }
        return 0.0;
    }

    @Override
    public int getReviewCount(Long productId) {
        if (productId == null) {
            return 0;
        }
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_AVG_RATING)) {
            ps.setLong(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(2);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error calculating review count for product ID {}", productId, e);
            throw new RuntimeException("Database error calculating review count", e);
        }
        return 0;
    }
}
