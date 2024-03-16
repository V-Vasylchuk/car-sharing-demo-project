package com.demo.carsharing.service;

import com.demo.carsharing.dto.request.CarRequestDto;
import com.demo.carsharing.dto.response.CarResponseDto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface CarService {
    CarResponseDto createCar(CarRequestDto carRequestDto, MultipartFile file);

    List<CarResponseDto> findAll();

    CarResponseDto findById(Long id);

    CarResponseDto update(CarRequestDto carRequestDto);

    void deleteById(Long id);

    void decreaseInventory(Long carId, int number);

    void increaseInventory(Long carId, int number);
}
