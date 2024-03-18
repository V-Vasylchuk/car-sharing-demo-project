package com.demo.carsharing.dto.mapper;

import com.demo.carsharing.dto.request.UserRequestDto;
import com.demo.carsharing.dto.response.UserResponseDto;
import com.demo.carsharing.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements DtoMapper<User, UserRequestDto, UserResponseDto> {
    @Override
    public User toModel(UserRequestDto requestDto) {
        return new User()
                .setId(requestDto.getId())
                .setFirstName(requestDto.getFirstName())
                .setLastName(requestDto.getLastName())
                .setEmail(requestDto.getEmail())
                .setPassword(requestDto.getPassword())
                .setRole(requestDto.getRole());
    }

    @Override
    public UserResponseDto toDto(User user) {
        return new UserResponseDto()
                .setId(user.getId())
                .setEmail(user.getEmail())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setRole(user.getRole());
    }
}
