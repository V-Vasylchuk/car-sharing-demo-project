package com.demo.carsharing.security;

import com.demo.carsharing.dto.request.UserRequestDto;
import com.demo.carsharing.exception.AuthenticationException;
import com.demo.carsharing.model.User;

public interface AuthenticationService {
    User register(UserRequestDto userRequestDto);

    User login(String login, String password) throws AuthenticationException;

    String encodePassword(String password);
}
