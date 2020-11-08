package com.gsg.youtubemonitor.dto.user;

import com.gsg.youtubemonitor.model.User;

public class UserHelper {

    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        return UserDto.builder()
                      .id(user.getId())
                      .username(user.getUsername())
                      .countryCode(user.getCountryCode())
                      .jobRunMinute(user.getJobRunMinute())
                      .build();
    }

    public static User fromDto(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        return User.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .countryCode(userDto.getCountryCode())
                .jobRunMinute(userDto.getJobRunMinute())
                .build();
    }
}
