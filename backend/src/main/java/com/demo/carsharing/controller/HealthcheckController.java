package com.demo.carsharing.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HealthcheckController {

    @GetMapping("/healthcheck")
    public String healthcheck() {
        log.info("I`m alive!!!");
        return "I`m alive!";
    }
}
