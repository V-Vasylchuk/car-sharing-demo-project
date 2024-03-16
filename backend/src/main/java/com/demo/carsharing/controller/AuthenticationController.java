package com.demo.carsharing.controller;

import com.demo.carsharing.dto.request.LoginRequestDto;
import com.demo.carsharing.dto.request.UserRequestDto;
import com.demo.carsharing.dto.response.UserResponseDto;
import com.demo.carsharing.exception.AuthenticationException;
import com.demo.carsharing.security.AuthenticationService;
import com.demo.carsharing.security.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registration of a new user (default role a CUSTOMER)")
    public UserResponseDto register(@RequestBody @Valid UserRequestDto userRequestDto) {
        log.debug("Try create new User");
        UserResponseDto userResponseDto = authenticationService.register(userRequestDto);
        log.debug("New User was successfully created");
        return userResponseDto;
    }

    @PostMapping("/login")
    @Operation(summary = "Get a JWT token for registration user")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        log.debug("Try get a JWT token for registration User");
        UserResponseDto userResponseDto;
        try {
            userResponseDto = authenticationService
                    .login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
            String token = jwtTokenProvider
                    .createToken(userResponseDto.getEmail(),
                            List.of(userResponseDto.getRole().name()));
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("timestamp", LocalDateTime.now().toString());
            body.put("token", token);
            body.put("status", HttpStatus.OK.value());
            log.debug("JWT token for registration User successfully got");
            return new ResponseEntity<>(body, HttpStatus.OK);
        } catch (AuthenticationException e) {
            throw new AuthenticationException("Invalid email or password!");
        }
    }
}
