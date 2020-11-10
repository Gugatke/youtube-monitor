package com.gsg.youtubemonitor.dto.auth;

import com.gsg.youtubemonitor.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private UserDto user;

    private String access;

    private String refresh;
}
