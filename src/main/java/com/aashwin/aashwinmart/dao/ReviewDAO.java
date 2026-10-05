package com.aashwin.aashwinmart.dao;

import com.aashwin.aashwinmart.model.Review;

import java.util.List;

/**
 * Data Access Object interface for Product Reviews.
 */
public interface ReviewDAO {

    Review create(Review review);

    List<Review> findByProductId(Long productId);

    boolean hasUserPurchasedProduct(Long userId, Long productId);

    boolean hasUserReviewedProduct(Long userId, Long productId);

    double getAverageRating(Long productId);

    int getReviewCount(Long productId);
}
