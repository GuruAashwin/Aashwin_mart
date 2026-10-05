package com.aashwin.aashwinmart.service;

import com.aashwin.aashwinmart.dao.DAOFactory;
import com.aashwin.aashwinmart.dao.ReviewDAO;
import com.aashwin.aashwinmart.dto.ReviewDTO;
import com.aashwin.aashwinmart.exception.AuthorizationException;
import com.aashwin.aashwinmart.exception.ValidationException;
import com.aashwin.aashwinmart.model.Review;
import com.aashwin.aashwinmart.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ReviewService enforcing verified buyer eligibility.
 */
public class ReviewServiceImpl implements ReviewService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewServiceImpl.class);
    private final ReviewDAO reviewDAO;

    public ReviewServiceImpl() {
        this.reviewDAO = DAOFactory.getReviewDAO();
    }

    public ReviewServiceImpl(ReviewDAO reviewDAO) {
        this.reviewDAO = reviewDAO;
    }

    @Override
    public ReviewDTO createReview(Long userId, Long productId, Integer rating, String comment) {
        if (userId == null || productId == null) {
            throw new IllegalArgumentException("User ID and Product ID are required");
        }
        ValidationUtil.validateRating(rating);
        ValidationUtil.requireNonEmpty(comment, "Review Comment");

        // Verified purchase check
        if (!reviewDAO.hasUserPurchasedProduct(userId, productId)) {
            LOGGER.warn("User ID {} attempted to review product ID {} without verified purchase", userId, productId);
            throw new AuthorizationException("You can only review products that you have purchased and received.");
        }

        // Duplicate review check
        if (reviewDAO.hasUserReviewedProduct(userId, productId)) {
            throw new ValidationException("You have already submitted a review for this product.");
        }

        Review review = new Review();
        review.setUserId(userId);
        review.setProductId(productId);
        review.setRating(rating);
        review.setComment(comment.trim());
        review.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        Review saved = reviewDAO.create(review);
        LOGGER.info("Created review ID {} for product ID {} by user ID {}", saved.getId(), productId, userId);
        return ReviewDTO.fromEntity(saved);
    }

    @Override
    public List<ReviewDTO> getProductReviews(Long productId) {
        if (productId == null) {
            return List.of();
        }
        return reviewDAO.findByProductId(productId).stream()
                .map(ReviewDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isEligibleToReview(Long userId, Long productId) {
        if (userId == null || productId == null) {
            return false;
        }
        return reviewDAO.hasUserPurchasedProduct(userId, productId) && !reviewDAO.hasUserReviewedProduct(userId, productId);
    }

    @Override
    public double getAverageRating(Long productId) {
        return reviewDAO.getAverageRating(productId);
    }

    @Override
    public int getReviewCount(Long productId) {
        return reviewDAO.getReviewCount(productId);
    }
}
