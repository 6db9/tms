package com.tms.service;

import com.tms.dao.UserDAO;
import com.tms.model.User;
import com.tms.util.PasswordUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class AuthServiceTest {

    @Test
    public void testLoginSuccess() throws Exception {
        // 1. Mock the DAO
        UserDAO mockDao = Mockito.mock(UserDAO.class);
        AuthService authService = new AuthService(mockDao); // We need to update AuthService to accept the DAO in constructor

        // 2. Setup fake user
        String salt = PasswordUtil.generateSalt();
        String hash = PasswordUtil.hash("secret123", salt);

        User fakeUser = new User();
        fakeUser.setUsername("test");
        fakeUser.setSalt(salt);
        fakeUser.setPasswordHash(hash);
        fakeUser.setActive(true);

        when(mockDao.findByUsername("test")).thenReturn(Optional.of(fakeUser));

        // 3. Test
        User result = authService.login("test", "secret123");
        assertNotNull(result);
        assertEquals("test", result.getUsername());
    }

    @Test
    public void testLoginWrongPassword() throws Exception {
        UserDAO mockDao = Mockito.mock(UserDAO.class);
        AuthService authService = new AuthService(mockDao);

        User fakeUser = new User();
        fakeUser.setSalt("salt");
        fakeUser.setPasswordHash("hash");
        fakeUser.setActive(true);

        when(mockDao.findByUsername("test")).thenReturn(Optional.of(fakeUser));

        // Assert that the specific exception is thrown
        assertThrows(AuthException.class, () -> {
            authService.login("test", "wrong_password");
        });
    }
}