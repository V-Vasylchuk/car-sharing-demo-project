package com.demo.carsharing.security;

import com.demo.carsharing.dto.response.UserResponseDto;
import com.demo.carsharing.service.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Try load User by email {}", email);
        Optional<UserResponseDto> userOptionalResponseDto =
                Optional.ofNullable(userService.findByEmail(email));
        UserBuilder builder;
        if (userOptionalResponseDto.isPresent()) {
            builder = org.springframework.security.core.userdetails.User.withUsername(email);
            builder.password(userOptionalResponseDto.get().getPassword());
            builder.roles(userOptionalResponseDto.get().getRole().name());
            log.debug("User was successfully load by email {}", email);
            return builder.build();
        }
        throw new UsernameNotFoundException("User not found.");
    }
}
