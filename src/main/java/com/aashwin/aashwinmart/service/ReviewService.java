package com.aashwin.aashwinmart.service;

import com.aashwin.aashwinmart.dto.ReviewDTO;

import java.util.List;

/**
 * Service interface for Verified Product Reviews and Ratings (F8).
 */
public interface ReviewService {

    ReviewDTO createReview(Long userId, Long productId, Integer rating, String comment);

    List<ReviewDTO> getProductReviews(Long productId);

    boolean isEligibleToReview(Long userId, Long productId);

    double getAverageRating(Long productId);

    int getReviewCount(Long productId);
}
