package com.gsg.youtubemonitor.security;

import com.gsg.youtubemonitor.common.YMException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private int expireMinutes;
    private JwtUtils jwtUtils;
    private int expireRefreshDays;

    @BeforeEach
    void setUp() {
        expireMinutes = 5;
        expireRefreshDays = 1;
        String key = "something";
        String refreshKey = "anotherThing";
        jwtUtils = Mockito.spy(new JwtUtils(expireMinutes, expireRefreshDays, key, refreshKey));
    }

    @Test
    void testValidatesCorrectlyWhenTokenValid() {
        String username = "superuser";
        String token = jwtUtils.generateJwt(username);
        boolean valid = jwtUtils.validate(token, username);
        assertTrue(valid);
    }

    @Test
    void testValidatesCorrectlyWhenWrongUsername() {
        String username = "correctUsername";
        String token = jwtUtils.generateJwt(username);
        String imposterUsername = "imposter";
        boolean valid = jwtUtils.validate(token, imposterUsername);
        assertFalse(valid);
    }

    @Test
    void testValidatesCorrectlyWhenExpired() {
        Mockito.when(jwtUtils.getCurrentDate())
                .then(invocationOnMock -> {
                    Calendar calendar = Calendar.getInstance();
                    int creationBefore = -expireMinutes - 2;
                    calendar.add(Calendar.MINUTE, creationBefore);
                    return calendar.getTime();
                });

        String username = "plainUsername";
        String token = jwtUtils.generateJwt(username);

        Mockito.when(jwtUtils.getCurrentDate()).thenCallRealMethod();
        boolean valid = jwtUtils.validate(token, username);
        assertFalse(valid);
    }

    @Test
    void testRefreshTokenCanGenerateValidToken() throws YMException {
        String username = "user";
        String refreshToken = jwtUtils.generateRefreshJwt(username);
        String usernameFromRefreshToken = jwtUtils.getUsernameFromRefreshToken(refreshToken);
        assertEquals(username, usernameFromRefreshToken);
    }

    @Test
    void testExpiredRefreshTokenCanNotBeUsed() {
        Mockito.when(jwtUtils.getCurrentDate())
                .then(invocationOnMock -> {
                    Calendar calendar = Calendar.getInstance();
                    int creationBefore = -expireRefreshDays - 2;
                    calendar.add(Calendar.DAY_OF_MONTH, creationBefore);
                    return calendar.getTime();
                });

        String username = "user";
        String refreshToken = jwtUtils.generateRefreshJwt(username);

        Mockito.when(jwtUtils.getCurrentDate()).thenCallRealMethod();
        assertThrows(YMException.class, () -> jwtUtils.getUsernameFromRefreshToken(refreshToken));
    }
}