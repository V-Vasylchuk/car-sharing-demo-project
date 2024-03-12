package com.demo.carsharing.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Response {
    private String timestamp;
    private String message;
    private int status;
}
