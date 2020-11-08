package com.gsg.youtubemonitor.service;

import com.gsg.youtubemonitor.common.YMException;
import com.gsg.youtubemonitor.dto.user.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDto getUser(int id);

    UserDto createUser(UserDto userDto) throws YMException;

    void updateUserCountry(int id, String country) throws YMException;
}
