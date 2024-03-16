package com.demo.carsharing.security;

import com.demo.carsharing.dto.request.UserRequestDto;
import com.demo.carsharing.dto.response.UserResponseDto;
import com.demo.carsharing.exception.AuthenticationException;

public interface AuthenticationService {
    UserResponseDto register(UserRequestDto userRequestDto);

    UserResponseDto login(String login, String password) throws AuthenticationException;

    String encodePassword(String password);
}
