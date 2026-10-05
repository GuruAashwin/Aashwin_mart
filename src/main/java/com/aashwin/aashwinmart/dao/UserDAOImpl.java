package com.aashwin.aashwinmart.dao;

import com.aashwin.aashwinmart.config.DatabaseConfig;
import com.aashwin.aashwinmart.model.Role;
import com.aashwin.aashwinmart.model.User;
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
 * JDBC implementation of UserDAO using PreparedStatement and HikariCP connection pool.
 */
public class UserDAOImpl implements UserDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDAOImpl.class);

    private static final String SQL_FIND_BY_EMAIL =
            "SELECT id, name, email, password_hash, role, is_active, created_at FROM users WHERE LOWER(email) = LOWER(?)";

    private static final String SQL_FIND_BY_ID =
            "SELECT id, name, email, password_hash, role, is_active, created_at FROM users WHERE id = ?";

    private static final String SQL_INSERT =
            "INSERT INTO users (name, email, password_hash, role, is_active, created_at) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE =
            "UPDATE users SET name = ?, email = ?, password_hash = ?, role = ?, is_active = ? WHERE id = ?";

    private static final String SQL_SET_ACTIVE =
            "UPDATE users SET is_active = ? WHERE id = ?";

    private static final String SQL_FIND_ALL =
            "SELECT id, name, email, password_hash, role, is_active, created_at FROM users ORDER BY created_at DESC";

    private static final String SQL_COUNT_ALL =
            "SELECT COUNT(*) FROM users";

    @Override
    public Optional<User> findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return Optional.empty();
        }
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_EMAIL)) {
            ps.setString(1, email.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding user by email: {}", email, e);
            throw new RuntimeException("Database error finding user by email", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding user by id: {}", id, e);
            throw new RuntimeException("Database error finding user by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public User create(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        Timestamp now = user.getCreatedAt() != null ? user.getCreatedAt() : new Timestamp(System.currentTimeMillis());
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail().trim().toLowerCase());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getRole() != null ? user.getRole().name() : Role.BUYER.name());
            ps.setBoolean(5, user.isActive());
            ps.setTimestamp(6, now);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                    user.setCreatedAt(now);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            return user;
        } catch (SQLException e) {
            LOGGER.error("Error creating user: {}", user.getEmail(), e);
            throw new RuntimeException("Database error creating user", e);
        }
    }

    @Override
    public boolean update(User user) {
        if (user == null || user.getId() == null) {
            return false;
        }
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail().trim().toLowerCase());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getRole().name());
            ps.setBoolean(5, user.isActive());
            ps.setLong(6, user.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error("Error updating user ID: {}", user.getId(), e);
            throw new RuntimeException("Database error updating user", e);
        }
    }

    @Override
    public boolean setUserActive(Long userId, boolean active) {
        if (userId == null) {
            return false;
        }
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SET_ACTIVE)) {
            ps.setBoolean(1, active);
            ps.setLong(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error("Error setting active={} for user ID: {}", active, userId, e);
            throw new RuntimeException("Database error toggling user active status", e);
        }
    }

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding all users", e);
            throw new RuntimeException("Database error finding all users", e);
        }
        return list;
    }

    @Override
    public long countAll() {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_COUNT_ALL);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            LOGGER.error("Error counting all users", e);
            throw new RuntimeException("Database error counting users", e);
        }
        return 0;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setRole(Role.fromString(rs.getString("role")));
        user.setActive(rs.getBoolean("is_active"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        return user;
    }
}
