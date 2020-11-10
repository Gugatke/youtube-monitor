package com.gsg.youtubemonitor.controller;

import com.gsg.youtubemonitor.common.YMException;
import com.gsg.youtubemonitor.common.YMExceptionReason;
import com.gsg.youtubemonitor.dto.auth.AuthRequest;
import com.gsg.youtubemonitor.dto.auth.AuthResponse;
import com.gsg.youtubemonitor.dto.auth.RefreshRequest;
import com.gsg.youtubemonitor.dto.user.UserDto;
import com.gsg.youtubemonitor.security.JwtUtils;
import com.gsg.youtubemonitor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
public class AuthenticateController {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final JwtUtils jwtUtils;

    @Autowired
    public AuthenticateController(AuthenticationManager authenticationManager,
                                  UserService userService,
                                  JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest authRequest) throws YMException {
        authenticateCredentials(authRequest);

        return createAuthResponse(authRequest.getUsername());
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest refreshRequest) throws YMException {
        String username = jwtUtils.getUsernameFromRefreshToken(refreshRequest.getRefresh());

        return createAuthResponse(username);
    }

    private ResponseEntity<AuthResponse> createAuthResponse(String username) {
        String jwtToken = jwtUtils.generateJwt(username);
        String refreshToken = jwtUtils.generateRefreshJwt(username);

        UserDto user = userService.getUserDto(username);
        AuthResponse response = AuthResponse.builder()
                .user(user)
                .access(jwtToken)
                .refresh(refreshToken)
                .build();
        return ResponseEntity.ok().body(response);
    }

    private void authenticateCredentials(AuthRequest authRequest) throws YMException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(),
                    authRequest.getPassword(),
                    Collections.emptyList()
            ));
        } catch (BadCredentialsException e) {
            throw new YMException(YMExceptionReason.BAD_REQUEST, "Incorrect username/password");
        }
    }
}
