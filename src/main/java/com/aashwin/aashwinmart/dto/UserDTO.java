package com.aashwin.aashwinmart.dto;

import com.aashwin.aashwinmart.model.Role;
import com.aashwin.aashwinmart.model.User;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Safe Data Transfer Object for Users (strictly excludes sensitive credential fields).
 * Implements Builder design pattern for clean construction.
 */
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String email;
    private Role role;
    private boolean active;
    private Timestamp createdAt;

    public UserDTO() {
    }

    public UserDTO(Long id, String name, String email, Role role, boolean active, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.active = active;
        this.createdAt = createdAt;
    }

    private UserDTO(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.email = builder.email;
        this.role = builder.role;
        this.active = builder.active;
        this.createdAt = builder.createdAt;
    }

    public static UserDTO fromEntity(User user) {
        if (user == null) {
            return null;
        }
        return new Builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Builder implementing the Builder design pattern for UserDTO construction.
     */
    public static class Builder {
        private Long id;
        private String name;
        private String email;
        private Role role;
        private boolean active = true;
        private Timestamp createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public Builder createdAt(Timestamp createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public UserDTO build() {
            return new UserDTO(this);
        }
    }
}
