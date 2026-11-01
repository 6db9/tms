package com.tms.util;

import com.tms.model.Role;
import com.tms.model.User;

import java.time.Duration;
import java.time.Instant;

public final class Session {

    public static final Duration INACTIVITY_TIMEOUT = Duration.ofMinutes(30);

    private static User currentUser;
    private static Instant lastActivity = Instant.now();

    private Session() {}

    public static void start(User user) {
        currentUser = user;
        touch();
    }

    public static void end() {
        currentUser = null;
    }

    public static User getUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        if (currentUser == null) return false;
        if (Duration.between(lastActivity, Instant.now()).compareTo(INACTIVITY_TIMEOUT) > 0) {
            end();
            return false;
        }
        return true;
    }

    public static void touch() {
        lastActivity = Instant.now();
    }

    /** Throws if the current user lacks the role. Authorization at execution point (spec rule). */
    public static void requireRole(Role role) {
        if (!isLoggedIn() || !currentUser.hasRole(role)) {
            throw new SecurityException("Accès refusé : rôle " + role + " requis.");
        }
        touch();
    }
}