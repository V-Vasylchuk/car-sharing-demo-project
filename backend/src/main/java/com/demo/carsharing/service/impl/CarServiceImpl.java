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
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
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
        log.debug("Try create new Car and save to DB");
        Car car = mapper.toModel(carRequestDto);
        String fileName = awsS3Service.upload(file);
        car = carRepository.save(car
                .setBucketName(clientS3Config.getBucketName())
                .setKeyName(fileName));
        log.debug("New Car {} was created and saved to DB", car);
        return mapper.toDto(car);
    }

    @Override
    public List<CarResponseDto> findAll() {
        log.debug("Try get all Cars from DB");
        List<Car> carList = carRepository.findAll();
        if (carList.isEmpty()) {
            throw new EntityNotFoundException("Cars not fount!");
        }
        carList.forEach(car ->
                car.setPresignedUrl(awsS3Service.getUrl(car.getBucketName(), car.getKeyName())));
        log.debug("All Cars was successfully got from DB");
        return carList.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public CarResponseDto findById(Long id) {
        log.debug("Try get Car by id {} from DB", id);
        Car car = carRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Car not found by ID " + id));
        car.setPresignedUrl(awsS3Service.getUrl(car.getBucketName(), car.getKeyName()));
        log.debug("Car by id {} was successfully got from DB", id);
        return mapper.toDto(car);
    }

    @Override
    @Transactional
    public CarResponseDto update(CarRequestDto carRequestDto) {
        log.debug("Try update Car by id {} from DB", carRequestDto.getId());
        Car car = carRepository.findById(carRequestDto.getId()).orElseThrow(() ->
                new DataProcessingException("Can't find carRequestDto with id: "
                        + carRequestDto.getId()));

        car = mapper.toModel(carRequestDto, car);
        car = carRepository.saveAndFlush(car);

        log.debug("Car by id {} was updated and saved to DB", carRequestDto.getId());
        return mapper.toDto(car);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.debug("Try delete Car by id {} from DB", id);
        try {
            carRepository.deleteById(id);
        } catch (EmptyResultDataAccessException exception) {
            throw new EntityNotFoundException("Car by id " + id
                    + " does not exist or has been deleted");
        }
        log.debug("Car by id {} was successfully deleted from DB", id);
    }

    @Override
    @Transactional
    public void decreaseInventory(Long carId, int number) {
        log.debug("Try decrease inventory for Car by id {} in DB", carId);
        Car car = carRepository.getReferenceById(carId);
        if (car.getInventory() > 0) {
            car.setInventory(car.getInventory() - number);
            carRepository.saveAndFlush(car);
            log.debug("Inventory was successfully decrease for Car by id {} in DB", carId);
        } else {
            throw new RuntimeException("No car available ");
        }
    }

    @Override
    @Transactional
    public void increaseInventory(Long carId, int number) {
        log.debug("Try increase inventory for Car by id {} in DB", carId);
        Car car = carRepository.getReferenceById(carId);
        car.setInventory(car.getInventory() + number);
        carRepository.saveAndFlush(car);
        log.debug("Inventory was successfully increase for Car by id {} in DB", carId);
    }
}
