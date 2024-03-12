package com.demo.carsharing.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
    private static final String REQUIRED_MESSAGE = "Required line, can`t be blank!";

    @NotBlank(message = REQUIRED_MESSAGE)
    @Email
    private String email;
    @NotBlank(message = REQUIRED_MESSAGE)
    private String firstName;
    @NotBlank(message = REQUIRED_MESSAGE)
    private String lastName;
    @Size(min = 6, message = "Must be minimum 6 character")
    private String password;
}
