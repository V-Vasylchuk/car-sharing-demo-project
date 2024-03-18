package com.demo.carsharing.service;

import com.demo.carsharing.dto.request.UserRequestDto;
import com.demo.carsharing.dto.response.UserResponseDto;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserService {
    UserResponseDto save(UserRequestDto userRequestDto);

    UserResponseDto findById(Long id);

    UserResponseDto findByEmail(String email);

    void delete(Long id);

    List<UserResponseDto> findAll();

    UserResponseDto update(UserRequestDto userRequestDto);

    UserResponseDto updateRole(Long id, String role);
}
