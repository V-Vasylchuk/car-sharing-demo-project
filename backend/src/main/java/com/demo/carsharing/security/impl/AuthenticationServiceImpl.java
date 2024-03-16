package com.demo.carsharing.security.impl;

import com.demo.carsharing.dto.mapper.DtoMapper;
import com.demo.carsharing.dto.request.UserRequestDto;
import com.demo.carsharing.dto.response.UserResponseDto;
import com.demo.carsharing.exception.AuthenticationException;
import com.demo.carsharing.model.User;
import com.demo.carsharing.security.AuthenticationService;
import com.demo.carsharing.service.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final DtoMapper<User, UserRequestDto, UserResponseDto> mapper;

    @Override
    public UserResponseDto register(UserRequestDto userRequestDto) {
        log.debug("Try register User and save to DB");
        userRequestDto.setRole(User.Role.CUSTOMER);
        userRequestDto.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        userService.save(userRequestDto);
        User user = mapper.toModel(userRequestDto);
        log.debug("User {} was register and saved to DB", user);
        return mapper.toDto(user);
    }

    @Override
    public UserResponseDto login(String login, String password) throws AuthenticationException {
        log.debug("Try login User");
        Optional<UserResponseDto> userResponseDto =
                Optional.ofNullable(userService.findByEmail(login));
        if (userResponseDto.isPresent()
                && passwordEncoder.matches(password, userResponseDto.get().getPassword())) {
            log.debug("User was  successfully login");
            return userResponseDto.get();
        }
        throw new AuthenticationException("Incorrect username or password!!!");
    }

    @Override
    public String encodePassword(String password) {
        log.debug("Try encode password");
        String encodePassword = passwordEncoder.encode(password);
        log.debug("Password was successfully encode");
        return encodePassword;
    }
}
