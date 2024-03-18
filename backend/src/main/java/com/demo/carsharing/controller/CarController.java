package com.demo.carsharing.controller;

import com.demo.carsharing.dto.request.CarRequestDto;
import com.demo.carsharing.dto.response.CarResponseDto;
import com.demo.carsharing.exception.DataProcessingException;
import com.demo.carsharing.service.CarService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/cars")
public class CarController {

    private CarService carService;
    private ObjectMapper objectMapper;

    @PostMapping()
    @Operation(summary = "Create a new car")
    public CarResponseDto create(@RequestPart(name = "car") String jsonObject,
                                 @RequestPart(name = "file") MultipartFile file) {
        log.debug("Try create new Car with MultipartFile");
        CarRequestDto carRequestDto;
        try {
            ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(String.valueOf(file)).toUriString();
            carRequestDto = objectMapper.readValue(jsonObject, CarRequestDto.class);
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<CarRequestDto>> violations
                    = validator.validate(carRequestDto);
            if (!violations.isEmpty()) {
                throw new DataProcessingException("Not valid data for create nev car");
            }
        } catch (JsonProcessingException exception) {
            throw new DataProcessingException("Invalid mapping", exception);
        }
        log.debug("New Car was successfully created");
        return carService.createCar(carRequestDto, file);
    }

    @GetMapping()
    @Operation(summary = "Get all cars")
    public List<CarResponseDto> getAll() {
        log.debug("Try get all Cars");
        List<CarResponseDto> carResponseDto = carService.findAll();
        log.debug("All Cars was successfully got");
        return carResponseDto;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a car by id")
    public CarResponseDto getById(@PathVariable Long id) {
        log.debug("Try get Car by id {}", id);
        CarResponseDto carResponseDto = carService.findById(id);
        log.debug("Car by id {} was successfully got", id);
        return carResponseDto;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a car")
    public CarResponseDto update(@Parameter(description = "Car id to update it:")
                                 @PathVariable Long id,
                                 @RequestBody @Valid CarRequestDto carRequestDto) {
        log.debug("Try update Car by id {}", id);
        carRequestDto.setId(id);
        CarResponseDto carResponseDto = carService.update(carRequestDto);
        log.debug("User was successfully updated by id {}", id);
        return carResponseDto;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a car")
    public void delete(@Parameter(description = "Car id to delete it:")
                       @PathVariable Long id) {
        log.debug("Try delete Car by id {}", id);
        carService.deleteById(id);
        log.debug("Car was deleted by id {}", id);
    }
}
