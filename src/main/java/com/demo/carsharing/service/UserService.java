package com.demo.carsharing.service;

import com.demo.carsharing.dto.request.UserRequestDto;
import com.demo.carsharing.dto.response.UserResponseDto;
import java.util.List;

public interface UserService {
    UserResponseDto save(UserRequestDto userRequestDto);

    UserResponseDto findById(Long id);

    UserResponseDto findByEmail(String email);

    void delete(Long id);

    List<UserResponseDto> findAll();

    UserResponseDto update(UserRequestDto userRequestDto);
}
