package com.demo.carsharing.dto.response;

import com.demo.carsharing.model.User;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UserResponseDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private User.Role role;
    private String password;
    private String chatId;
}
