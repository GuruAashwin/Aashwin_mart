package com.aashwin.aashwinmart.service;

import com.aashwin.aashwinmart.dao.DAOFactory;
import com.aashwin.aashwinmart.dao.UserDAO;
import com.aashwin.aashwinmart.dto.UserDTO;
import com.aashwin.aashwinmart.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of UserService for retrieving and managing user data.
 */
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserDAO userDAO;

    public UserServiceImpl() {
        this.userDAO = DAOFactory.getUserDAO();
    }

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDTO getUserById(Long id) {
        return userDAO.findById(id)
                .map(UserDTO::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return userDAO.findByEmail(email)
                .map(UserDTO::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userDAO.findAll().stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public long countAllUsers() {
        return userDAO.countAll();
    }

    @Override
    public boolean setUserActive(Long userId, boolean active) {
        if (userId == null) {
            return false;
        }
        boolean result = userDAO.setUserActive(userId, active);
        LOGGER.info("User ID {} active status set to {} by admin action", userId, active);
        return result;
    }
}
