package com.gsg.youtubemonitor.controller;

import com.gsg.youtubemonitor.common.YMException;
import com.gsg.youtubemonitor.common.YMExceptionReason;
import com.gsg.youtubemonitor.dto.data.CountryDataDto;
import com.gsg.youtubemonitor.dto.user.UserDto;
import com.gsg.youtubemonitor.dto.user.UserHelper;
import com.gsg.youtubemonitor.model.User;
import com.gsg.youtubemonitor.service.CountryDataService;
import com.gsg.youtubemonitor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    private final UserService userService;

    private final CountryDataService countryDataService;

    @Autowired
    public UserController(UserService userService, CountryDataService countryDataService) {
        this.userService = userService;
        this.countryDataService = countryDataService;
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) throws YMException {
        UserDto createdUser = userService.createUser(userDto);
        return ResponseEntity.ok(createdUser);
    }

    @RequestMapping(value = "/secured/users/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Integer userId) throws YMException {
        if (userId == null) {
            throw new YMException(YMExceptionReason.BAD_REQUEST, "UserId should not be null");
        }
        User foundUser = userService.getUser(userId);
        if (foundUser == null) {
            throw new YMException(YMExceptionReason.BAD_REQUEST, String.format("User with id[%d] not found", userId));
        }
        UserDto userDto = UserHelper.toDto(foundUser);
        return ResponseEntity.ok(userDto);
    }

    @RequestMapping(value = "/secured/users/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@PathVariable("id") Integer id,
                                        @RequestParam("countryCode") String countryCode,
                                        @RequestParam("jobRunMinute") Integer jobRunMinute) throws YMException {
        if (id == null) {
            throw new YMException(YMExceptionReason.BAD_REQUEST, "User id should not be null");
        }
        userService.updateUser(id, countryCode, jobRunMinute);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/secured/users/{id}/countryData")
    public ResponseEntity<CountryDataDto> getUserCountryData(@PathVariable("id") Integer userId) throws YMException {
        CountryDataDto countryDataDto = countryDataService.getCountryDataDto(userId);
        return ResponseEntity.ok(countryDataDto);
    }
}
