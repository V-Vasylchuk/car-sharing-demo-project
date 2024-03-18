package com.demo.carsharing.dto.mapper;

import com.demo.carsharing.dto.request.CarRequestDto;
import com.demo.carsharing.dto.response.CarResponseDto;
import com.demo.carsharing.model.Car;
import org.springframework.stereotype.Component;

@Component
public class CarMapper implements DtoMapper<Car, CarRequestDto, CarResponseDto> {
    @Override
    public Car toModel(CarRequestDto requestDto) {
        return toModel(requestDto, new Car());
    }

    @Override
    public Car toModel(CarRequestDto requestDto, Car model) {
        return model
                .setId(requestDto.getId())
                .setModel(requestDto.getModel())
                .setBrand(requestDto.getBrand())
                .setType(requestDto.getType())
                .setInventory(requestDto.getInventory())
                .setDailyFee(requestDto.getDailyFee())
                .setBucketName(requestDto.getBucketName())
                .setKeyName(requestDto.getKeyName())
                .setPresignedUrl(requestDto.getPresignedUrl());
    }

    @Override
    public CarResponseDto toDto(Car model) {
        return new CarResponseDto()
                .setId(model.getId())
                .setModel(model.getModel())
                .setBrand(model.getBrand())
                .setType(model.getType())
                .setInventory(model.getInventory())
                .setDailyFee(model.getDailyFee())
                .setBucketName(model.getBucketName())
                .setKeyName(model.getKeyName())
                .setPresignedUrl(model.getPresignedUrl());
    }
}
