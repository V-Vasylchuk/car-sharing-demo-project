package com.demo.carsharing.service.impl;

import com.demo.carsharing.config.AwsClientS3Config;
import com.demo.carsharing.dto.mapper.DtoMapper;
import com.demo.carsharing.dto.request.CarRequestDto;
import com.demo.carsharing.dto.response.CarResponseDto;
import com.demo.carsharing.exception.DataProcessingException;
import com.demo.carsharing.model.Car;
import com.demo.carsharing.repository.CarRepository;
import com.demo.carsharing.service.AwsS3Service;
import com.demo.carsharing.service.CarService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final AwsS3Service awsS3Service;
    private final AwsClientS3Config clientS3Config;
    private final DtoMapper<Car, CarRequestDto, CarResponseDto> mapper;

    @Override
    @Transactional
    public CarResponseDto createCar(CarRequestDto carRequestDto, MultipartFile file) {
        Car car = mapper.toModel(carRequestDto);
        String fileName = awsS3Service.upload(file);
        car.setBucketName(clientS3Config.getBucketName());
        car.setKeyName(fileName);
        carRepository.save(car);
        return mapper.toDto(car);
    }

    @Override
    public List<CarResponseDto> findAll() {
        List<Car> carList = carRepository.findAll();
        carList.forEach(car ->
                car.setPresignedUrl(awsS3Service.getUrl(car.getBucketName(), car.getKeyName())));
        return carList.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public CarResponseDto findById(Long id) {
        Car car = carRepository.getReferenceById(id);
        car.setPresignedUrl(awsS3Service.getUrl(car.getBucketName(), car.getKeyName()));
        return mapper.toDto(car);
    }

    @Override
    @Transactional
    public CarResponseDto update(CarRequestDto carRequestDto) {
        Car car = mapper.toModel(carRequestDto);
        car = carRepository.findById(car.getId()).orElseThrow(() ->
                new DataProcessingException("Can't find carRequestDto with id: "
                        + carRequestDto.getId()));
        carRepository.saveAndFlush(car);
        return mapper.toDto(car);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        carRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void decreaseInventory(Long carId, int number) {
        Car car = carRepository.getReferenceById(carId);
        if (car.getInventory() > 0) {
            car.setInventory(car.getInventory() - number);
            carRepository.saveAndFlush(car);
        } else {
            throw new RuntimeException("No car available ");
        }
    }

    @Override
    @Transactional
    public void increaseInventory(Long carId, int number) {
        Car car = carRepository.getReferenceById(carId);
        car.setInventory(car.getInventory() + number);
        carRepository.saveAndFlush(car);
    }
}
