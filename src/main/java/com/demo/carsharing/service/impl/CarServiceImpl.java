package com.demo.carsharing.service.impl;

import com.demo.carsharing.config.AwsClientS3Config;
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


    @Override
    @Transactional
    public Car createCar(Car car, MultipartFile file) {
        String fileName = awsS3Service.upload(file);
        car.setBucketName(clientS3Config.getBucketName());
        car.setKeyName(fileName);
        return carRepository.save(car);
    }

    @Override
    public List<Car> findAll() {
        List<Car> carList = carRepository.findAll();
        carList.forEach(car -> car.setImageUrl(awsS3Service.getUrl(car.getBucketName(), car.getKeyName())));
        return carList;
    }

    @Override
    public Car findById(Long id) {
        Car car = carRepository.getReferenceById(id);
        car.setPresignedUrl(awsS3Service.getUrl(car.getBucketName(), car.getKeyName()));
        return car;
    }

    @Override
    @Transactional
    public Car update(Car car) {
        carRepository.findById(car.getId()).orElseThrow(() ->
                new DataProcessingException("Can't find car with id: " + car.getId()));
        return carRepository.saveAndFlush(car);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        carRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void decreaseInventory(Long carId, int number) {
        Car car = findById(carId);
        if (car.getInventory() > 0) {
            car.setInventory(car.getInventory() - number);
            update(car);
        } else {
            throw new RuntimeException("No car available ");
        }
    }

    @Override
    @Transactional
    public void increaseInventory(Long carId, int number) {
        Car car = findById(carId);
        car.setInventory(car.getInventory() + number);
        update(car);
    }
}
