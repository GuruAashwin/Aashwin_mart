package com.aashwin.aashwinmart.service;

import com.aashwin.aashwinmart.dto.UserDTO;

import java.util.List;

/**
 * Service interface for User profile and Admin user management.
 */
public interface UserService {

    UserDTO getUserById(Long id);

    UserDTO getUserByEmail(String email);

    List<UserDTO> getAllUsers();

    long countAllUsers();

    /**
     * Activates or deactivates a user account (Admin only).
     *
     * @param userId Target user ID
     * @param active true to activate, false to deactivate
     * @return true if update was successful
     */
    boolean setUserActive(Long userId, boolean active);
}
