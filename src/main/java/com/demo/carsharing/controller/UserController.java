package com.demo.carsharing.controller;

import com.demo.carsharing.dto.mapper.DtoMapper;
import com.demo.carsharing.dto.request.UserRequestDto;
import com.demo.carsharing.dto.response.UserResponseDto;
import com.demo.carsharing.model.User;
import com.demo.carsharing.security.AuthenticationService;
import com.demo.carsharing.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final DtoMapper<User, UserRequestDto, UserResponseDto> userMapper;

    @GetMapping("/me")
    @Operation(summary = "Get my profile info by token")
    public UserResponseDto get(Authentication authentication) {
        return userMapper.toDto(userService.findByEmail(authentication.getName()).get());
    }

    @PutMapping("/me")
    @Operation(summary = "Update profile info by token")
    public UserResponseDto update(Authentication authentication,
                                  @RequestBody @Valid UserRequestDto userRequestDto) {
        Long userId = userService.findByEmail(authentication.getName()).get().getId();
        User user = userMapper.toModel(userRequestDto)
                .setId(userId)
                .setRole(userService.findById(userId).getRole())
                .setPassword(authenticationService.encodePassword(userRequestDto.getPassword()));
        return userMapper.toDto(userService.update(user));
    }

    @PutMapping("/{id}/role")
    @Operation(summary = "Update user role")
    public UserResponseDto updateRole(@PathVariable Long id, @RequestParam("role") String role) {
        try {
            User user = userService.findById(id);
            user.setRole(User.Role.valueOf(role));
            return userMapper.toDto(userService.update(user));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(String.format("Invalid user role '%s'. Used only '%s'.",
                    role, Arrays.toString(User.Role.values())));
        }
    }
}
