package com.gsg.youtubemonitor.service;

import com.gsg.youtubemonitor.common.YMException;
import com.gsg.youtubemonitor.dto.user.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void testUserIsCreated() throws YMException {
        UserDto userDto = createTestUser("1");

        UserDto createdUserDto = userService.createUser(userDto);
        UserDto userFromDb = userService.getUserDto(createdUserDto.getUsername());
        assertNotNull(userFromDb);
        assertEquals(userDto.getUsername(), userFromDb.getUsername());
    }

    @Test
    void testUserIsCreatedWithValidId() throws YMException {
        UserDto userDto = createTestUser("2 ");
        userDto.setId(402);

        UserDto createdUser = userService.createUser(userDto);

        assertNotEquals(userDto.getId(), createdUser.getId());
    }


    @Test
    void testUserIsNotCreatedForDuplicateId() {
        UserDto userDto = createTestUser("3");
        userDto.setId(1); // set id which we know is occupied

        assertThrows(YMException.class, () -> userService.createUser(userDto));
    }

    @Test
    void testGetUserDetailsWork() {
        String username = "admin";
        UserDetails userDetails = userService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
    }

    @Test
    void testGetUserDetailsThrowsUserNotFoundException() {
        assertThrows(UsernameNotFoundException.class, () -> {
            String notExistingUsername = "nonExistentUser";
            userService.loadUserByUsername(notExistingUsername);
        });
    }

    @Test
    void testCountryCodeIsUpdatedCorrectly() throws YMException {
        String countryCode = "US";
        userService.updateUser(1, countryCode, null);
        UserDto userDto = userService.getUserDto("admin");
        assertEquals(countryCode, userDto.getCountryCode());
    }

    @Test
    void testCountryCodeUpdateFailsOnWrongId() {
        assertThrows(YMException.class, () -> userService.updateUser(2, "US", null));
    }

    private UserDto createTestUser(String username) {
        return UserDto.builder()
                .username(username)
                .password("testPassword")
                .countryCode("GE")
                .jobRunMinute(1)
                .build();
    }
}