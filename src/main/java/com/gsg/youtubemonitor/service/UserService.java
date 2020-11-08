package com.gsg.youtubemonitor.service;

import com.gsg.youtubemonitor.common.YMException;
import com.gsg.youtubemonitor.dto.user.UserDto;
import com.gsg.youtubemonitor.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserDto getUserDto(int id);

    UserDto createUser(UserDto userDto) throws YMException;

    void updateUser(int id, String country, Integer jobRunMinute) throws YMException;

    List<Integer> getUserIdsToRunJobs();

    User getUser(int id);
}
