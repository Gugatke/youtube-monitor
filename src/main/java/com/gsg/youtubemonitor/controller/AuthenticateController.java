package com.gsg.youtubemonitor.controller;

import com.gsg.youtubemonitor.common.YMException;
import com.gsg.youtubemonitor.common.YMExceptionReason;
import com.gsg.youtubemonitor.dto.AuthRequest;
import com.gsg.youtubemonitor.dto.AuthResponse;
import com.gsg.youtubemonitor.dto.RefreshRequest;
import com.gsg.youtubemonitor.security.JwtUtils;
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

    private final JwtUtils jwtUtils;

    @Autowired
    public AuthenticateController(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @RequestMapping(value = "/session/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest authRequest) throws YMException {
        authenticateCredentials(authRequest);

        String jwtToken = jwtUtils.generateJwt(authRequest.getUsername());
        String refreshToken = jwtUtils.generateRefreshJwt(authRequest.getUsername());

        AuthResponse response = AuthResponse.builder()
                                            .jwtToken(jwtToken)
                                            .refreshToken(refreshToken)
                                            .build();
        return ResponseEntity.ok().body(response);
    }

    @RequestMapping(value = "/session/refresh", method = RequestMethod.POST)
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest refreshRequest) throws YMException {
        String jwtToken = jwtUtils.getTokenFromRefreshToken(refreshRequest.getRefreshToken());

        AuthResponse response = AuthResponse.builder()
                                            .jwtToken(jwtToken)
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
