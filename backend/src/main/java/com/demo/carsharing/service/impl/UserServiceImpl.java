package com.demo.carsharing.service.impl;

import com.demo.carsharing.dto.mapper.DtoMapper;
import com.demo.carsharing.dto.request.UserRequestDto;
import com.demo.carsharing.dto.response.UserResponseDto;
import com.demo.carsharing.exception.DataProcessingException;
import com.demo.carsharing.model.User;
import com.demo.carsharing.repository.UserRepository;
import com.demo.carsharing.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final DtoMapper<User, UserRequestDto, UserResponseDto> mapper;

    @Override
    @Transactional
    public UserResponseDto save(UserRequestDto userRequestDto) {
        User user = mapper.toModel(userRequestDto);
        userRepository.save(user);
        return mapper.toDto(user);
    }

    @Override
    public UserResponseDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new DataProcessingException("Can't find user by id: '%s'", id));
        return mapper.toDto(user);
    }

    @Override
    public UserResponseDto findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new DataProcessingException("Can't find user by email: '%s'", email));
        return mapper.toDto(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserResponseDto> findAll() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public UserResponseDto update(UserRequestDto userRequestDto) {
        User user = userRepository.findById(userRequestDto.getId())
                .orElseThrow(() ->
                        new DataProcessingException("Not found user with id:%s for update",
                                userRequestDto.getId()));
        userRepository.save(user);
        return mapper.toDto(user);
    }
}
