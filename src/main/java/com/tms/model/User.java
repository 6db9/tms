package com.tms.model;

import java.util.EnumSet;
import java.util.Set;

public class User {
    private int id;
    private String username;
    private String passwordHash;
    private String salt;
    private boolean active;
    private final Set<Role> roles = EnumSet.noneOf(Role.class);

    public User() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getSalt() { return salt; }
    public void setSalt(String salt) { this.salt = salt; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Set<Role> getRoles() { return roles; }

    public boolean hasRole(Role role) { return roles.contains(role); }

    @Override
    public String toString() {
        return username + " " + roles;
    }
}