package com.demo.carsharing.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class StripeResponseDto<T> {

    private String status;
    private String message;
    private Integer httpStatus;
    private T data;
}
