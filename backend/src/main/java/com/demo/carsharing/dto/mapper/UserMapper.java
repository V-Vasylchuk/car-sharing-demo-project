package com.demo.carsharing.dto.mapper;

import com.demo.carsharing.dto.request.UserRequestDto;
import com.demo.carsharing.dto.response.UserResponseDto;
import com.demo.carsharing.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements DtoMapper<User, UserRequestDto, UserResponseDto> {
    @Override
    public User toModel(UserRequestDto requestDto) {
        return toModel(requestDto, new User());
    }

    @Override
    public User toModel(UserRequestDto requestDto, User model) {
        return model
                .setFirstName(requestDto.getFirstName())
                .setLastName(requestDto.getLastName())
                .setEmail(requestDto.getEmail())
                .setPassword(requestDto.getPassword())
                .setRole(requestDto.getRole())
                .setChatId(requestDto.getChatId());
    }

    @Override
    public UserResponseDto toDto(User user) {
        return new UserResponseDto()
                .setId(user.getId())
                .setEmail(user.getEmail())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setRole(user.getRole())
                .setPassword(user.getPassword())
                .setChatId(user.getChatId());
    }
}
