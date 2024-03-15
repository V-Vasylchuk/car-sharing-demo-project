package com.demo.carsharing.controller;

import com.demo.carsharing.dto.mapper.DtoMapper;
import com.demo.carsharing.dto.request.CarRequestDto;
import com.demo.carsharing.dto.response.CarResponseDto;
import com.demo.carsharing.model.Car;
import com.demo.carsharing.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/cars")
public class CarController {
    private final DtoMapper<Car, CarRequestDto, CarResponseDto> mapper;
    private CarService carService;

    @PostMapping()
    @Operation(summary = "Create a new car")
    public CarResponseDto create(@RequestBody @Valid CarRequestDto carRequestDto) {
        return mapper.toDto(carService.createCar(mapper.toModel(carRequestDto)));
    }

    @GetMapping()
    @Operation(summary = "Get all cars")
    public List<CarResponseDto> getAll() {
        return carService.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a car by id")
    public CarResponseDto getById(@PathVariable Long id) {
        return mapper.toDto(carService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a car")
    public CarResponseDto update(@Parameter(description = "Car id to update it:")
                                    @PathVariable Long id,
                                 @RequestBody @Valid CarRequestDto carRequestDto) {
        Car car = mapper.toModel(carRequestDto)
                        .setId(id);

        return mapper.toDto(carService
                     .update(car));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a car")
    public void delete(@Parameter(description = "Car id to delete it:")
                           @PathVariable Long id) {
        carService.deleteById(id);
    }
}
