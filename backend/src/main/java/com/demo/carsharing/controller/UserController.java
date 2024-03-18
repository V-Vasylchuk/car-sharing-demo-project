package com.demo.carsharing.controller;

import com.demo.carsharing.dto.request.UserRequestDto;
import com.demo.carsharing.dto.response.UserResponseDto;
import com.demo.carsharing.model.User;
import com.demo.carsharing.security.AuthenticationService;
import com.demo.carsharing.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id")
    public UserResponseDto getById(@PathVariable Long id) {
        log.debug("Try get User by id {}", id);
        UserResponseDto userResponseDto = userService.findById(id);
        log.debug("User by id {} was successfully got", id);
        return userResponseDto;
    }

    @GetMapping("/me")
    @Operation(summary = "Get my profile info by token")
    public UserResponseDto get(Authentication authentication) {
        log.debug("Try get User by email {}", authentication.getName());
        UserResponseDto userResponseDto = userService.findByEmail(authentication.getName());
        log.debug("User was successfully got by email {}", authentication.getName());
        return userResponseDto;
    }

    @PutMapping("/me")
    @Operation(summary = "Update profile info by token")
    public UserResponseDto update(Authentication authentication,
                                  @RequestBody @Valid UserRequestDto userRequestDto) {
        log.debug("Try update User by token");
        Long userId = userService.findByEmail(authentication.getName()).getId();
        userRequestDto.setId(userId);
        userRequestDto.setRole(userService.findById(userId).getRole());
        userRequestDto.setPassword(authenticationService.encodePassword(
                userRequestDto.getPassword()));
        UserResponseDto userResponseDto = userService.update(userRequestDto);
        log.debug("User was successfully updated by token");
        return userResponseDto;
    }

    @PutMapping("/{id}/role")
    @Operation(summary = "Update user role")
    public UserResponseDto updateRole(@PathVariable Long id, @RequestParam("role") String role) {
        log.debug("Try update User Role");
        try {
            UserResponseDto userResponseDto = userService.updateRole(id, role);
            log.debug("User Role was successfully updated");
            return userResponseDto;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(String.format("Invalid user role '%s'. Used only '%s'.",
                    role, Arrays.toString(User.Role.values())));
        }
    }
}
