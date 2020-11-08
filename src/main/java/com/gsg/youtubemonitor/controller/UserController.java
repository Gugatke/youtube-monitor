package com.gsg.youtubemonitor.controller;

import com.gsg.youtubemonitor.common.YMException;
import com.gsg.youtubemonitor.dto.user.UserDto;
import com.gsg.youtubemonitor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDto userDto) throws YMException {
        UserDto createdUser = userService.createUser(userDto);
        return ResponseEntity.ok(createdUser);
    }

    @RequestMapping(value = "users/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@PathVariable("id") int id,
                                        @RequestParam("countryCode") String countryCode) throws YMException {
        userService.updateUserCountry(id, countryCode);
        return ResponseEntity.ok().build();
    }
}
