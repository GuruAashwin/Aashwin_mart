package com.aashwin.aashwinmart.service;

import com.aashwin.aashwinmart.dto.UserDTO;
import com.aashwin.aashwinmart.model.User;

/**
 * Service interface for Authentication and User Registration (F1).
 */
public interface AuthService {

    /**
     * Authenticates a user by email and plaintext password.
     *
     * @param email User email
     * @param plainPassword Plaintext password
     * @return Authenticated User entity
     */
    User login(String email, String plainPassword);

    /**
     * Registers a new Buyer account.
     *
     * @param name Full name
     * @param email Email address
     * @param password Plaintext password
     * @param confirmPassword Confirmation password
     * @return Registered UserDTO
     */
    UserDTO registerBuyer(String name, String email, String password, String confirmPassword);

    /**
     * Registers a new Seller account.
     *
     * @param name Business or seller name
     * @param email Email address
     * @param password Plaintext password
     * @param confirmPassword Confirmation password
     * @return Registered UserDTO
     */
    UserDTO registerSeller(String name, String email, String password, String confirmPassword);
}
