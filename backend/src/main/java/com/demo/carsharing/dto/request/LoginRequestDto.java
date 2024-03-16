package com.demo.carsharing.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
    @NotBlank(message = "can't be blank")
    private String email;
    @NotBlank(message = "can't be blank")
    private String password;
}
