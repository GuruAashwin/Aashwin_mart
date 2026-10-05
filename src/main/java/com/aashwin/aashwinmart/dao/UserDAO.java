package com.aashwin.aashwinmart.dao;

import com.aashwin.aashwinmart.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for User entities.
 */
public interface UserDAO {

    /**
     * Finds a user by unique email address.
     *
     * @param email User email
     * @return Optional containing User if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds a user by primary key ID.
     *
     * @param id User ID
     * @return Optional containing User if found
     */
    Optional<User> findById(Long id);

    /**
     * Inserts a new user record.
     *
     * @param user User entity to persist
     * @return Persisted user with generated ID
     */
    User create(User user);

    /**
     * Updates an existing user's profile information.
     *
     * @param user User entity with updated fields
     * @return true if updated successfully
     */
    boolean update(User user);

    /**
     * Retrieves all users in the system (ordered by registration date descending).
     *
     * @return List of all users
     */
    List<User> findAll();

    /**
     * Toggles a user's active/inactive status.
     *
     * @param userId User ID
     * @param active true to activate, false to deactivate
     * @return true if updated
     */
    boolean setUserActive(Long userId, boolean active);

    /**
     * Counts total number of registered users.
     *
     * @return Total user count
     */
    long countAll();
}
