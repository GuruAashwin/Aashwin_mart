package com.aashwin.aashwinmart.service;

import com.aashwin.aashwinmart.dao.DAOFactory;
import com.aashwin.aashwinmart.dao.UserDAO;
import com.aashwin.aashwinmart.dto.UserDTO;
import com.aashwin.aashwinmart.exception.AuthenticationException;
import com.aashwin.aashwinmart.exception.ValidationException;
import com.aashwin.aashwinmart.model.Role;
import com.aashwin.aashwinmart.model.User;
import com.aashwin.aashwinmart.util.PasswordUtil;
import com.aashwin.aashwinmart.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * Implementation of AuthService with bcrypt password verification, input validation, and email uniqueness checks.
 */
public class AuthServiceImpl implements AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final UserDAO userDAO;

    public AuthServiceImpl() {
        this.userDAO = DAOFactory.getUserDAO();
    }

    public AuthServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User login(String email, String plainPassword) {
        ValidationUtil.requireNonEmpty(email, "Email");
        ValidationUtil.requireNonEmpty(plainPassword, "Password");

        Optional<User> userOpt = userDAO.findByEmail(email.trim().toLowerCase());
        if (userOpt.isEmpty()) {
            LOGGER.warn("Login failed: email '{}' not found", email);
            throw new AuthenticationException("Invalid email or password");
        }

        User user = userOpt.get();
        if (!PasswordUtil.checkPassword(plainPassword, user.getPasswordHash())) {
            LOGGER.warn("Login failed: invalid password for user '{}'", email);
            throw new AuthenticationException("Invalid email or password");
        }

        if (!user.isActive()) {
            LOGGER.warn("Login failed: account '{}' is suspended", email);
            throw new AuthenticationException("Your account has been suspended. Please contact support.");
        }

        LOGGER.info("User '{}' [{}] logged in successfully", user.getEmail(), user.getRole());
        return user;
    }

    @Override
    public UserDTO registerBuyer(String name, String email, String password, String confirmPassword) {
        return registerUser(name, email, password, confirmPassword, Role.BUYER);
    }

    @Override
    public UserDTO registerSeller(String name, String email, String password, String confirmPassword) {
        return registerUser(name, email, password, confirmPassword, Role.SELLER);
    }

    private UserDTO registerUser(String name, String email, String password, String confirmPassword, Role role) {
        ValidationUtil.requireNonEmpty(name, "Name");
        ValidationUtil.validateEmail(email);
        ValidationUtil.validatePasswordConfirmation(password, confirmPassword);

        String normalizedEmail = email.trim().toLowerCase();
        if (userDAO.findByEmail(normalizedEmail).isPresent()) {
            LOGGER.warn("Registration failed: email '{}' already registered", normalizedEmail);
            throw new ValidationException("An account with this email address already exists");
        }

        String passwordHash = PasswordUtil.hashPassword(password);
        User user = new User();
        user.setName(name.trim());
        user.setEmail(normalizedEmail);
        user.setPasswordHash(passwordHash);
        user.setRole(role);
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        User created = userDAO.create(user);
        LOGGER.info("Successfully registered new user: {} with role: {}", created.getEmail(), created.getRole());
        return UserDTO.fromEntity(created);
    }
}
