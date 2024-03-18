package com.demo.carsharing.service.impl;

import com.demo.carsharing.dto.mapper.DtoMapper;
import com.demo.carsharing.dto.request.UserRequestDto;
import com.demo.carsharing.dto.response.UserResponseDto;
import com.demo.carsharing.exception.DataProcessingException;
import com.demo.carsharing.model.User;
import com.demo.carsharing.repository.UserRepository;
import com.demo.carsharing.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final DtoMapper<User, UserRequestDto, UserResponseDto> mapper;

    @Override
    @Transactional
    public UserResponseDto save(UserRequestDto userRequestDto) {
        log.debug("Try create new User and save to DB");
        User user = mapper.toModel(userRequestDto);
        user = userRepository.save(user);
        log.debug("New User {} was created and saved to DB", user);
        return mapper.toDto(user);
    }

    @Override
    public UserResponseDto findById(Long id) {
        log.debug("Try get User by id {} from DB", id);
        User user = userRepository.findById(id).orElseThrow(() ->
                new DataProcessingException("Can't find user by id: '%s'", id));
        log.debug("User by id {} was successfully got from DB", id);
        return mapper.toDto(user);
    }

    @Override
    public UserResponseDto findByEmail(String email) {
        log.debug("Try get User by email {} from DB", email);
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new DataProcessingException("Can't find user by email: '%s'", email));
        log.debug("User by email {} was successfully got from DB", email);
        return mapper.toDto(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Try delete User by id {} from DB", id);
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException exception) {
            throw new EntityNotFoundException("User by id " + id
                    + " does not exist or has been deleted");
        }
        log.debug("User by id {} was successfully deleted from DB", id);
    }

    @Override
    public List<UserResponseDto> findAll() {
        log.debug("Try get all Users from DB");
        List<User> userList = userRepository.findAll();
        if (userList.isEmpty()) {
            throw new EntityNotFoundException("Cars not fount!");
        }
        log.debug("All Users was successfully got from DB");
        return userList.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public UserResponseDto update(UserRequestDto userRequestDto) {
        log.debug("Try update User by id {} from DB", userRequestDto.getId());
        User user = userRepository.findById(userRequestDto.getId())
                .orElseThrow(() ->
                        new DataProcessingException("Not found user with id:%s for update",
                                userRequestDto.getId()));

        user = mapper.toModel(userRequestDto, user);
        user = userRepository.saveAndFlush(user);
        log.debug("User by id {} was updated and saved to DB", userRequestDto.getId());
        return mapper.toDto(user);
    }

    @Override
    public UserResponseDto updateRole(Long id, String role) {
        User.Role userRole = User.Role.valueOf(role);log.debug("Try update User Role by id {} from DB", id);
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new DataProcessingException("Not found user with id:%s for update", id));

        user.setRole(userRole);
        user = userRepository.saveAndFlush(user);
        log.debug("User by id {} was updated and saved to DB", user.getId());

        return mapper.toDto(user);
    }
}
