package com.tms.service;

import com.tms.dao.UserDAO;
import com.tms.model.User;
import com.tms.util.PasswordUtil;

import java.sql.SQLException;

public class AuthService {

    private final UserDAO userDAO = new UserDAO();

    /**
     * Returns the authenticated user, or throws AuthException.
     * Note: on any failure (unknown, wrong password, disabled) the same
     * generic message is used — spec F1 rule.
     */
    public User login(String username, String password) throws AuthException {
        if (username == null || password == null ||
                username.isBlank() || password.isBlank()) {
            throw new AuthException("Identifiants invalides.");
        }
        try {
            User u = userDAO.findByUsername(username.trim())
                    .orElseThrow(() -> new AuthException("Identifiants invalides."));

            if (!PasswordUtil.verify(password, u.getSalt(), u.getPasswordHash())) {
                throw new AuthException("Identifiants invalides.");
            }
            if (!u.isActive()) {
                throw new AuthException("Identifiants invalides.");
            }
            return u;
        } catch (SQLException e) {
            throw new AuthException("Erreur technique lors de la connexion. Réessayez.");
        }
    }
}