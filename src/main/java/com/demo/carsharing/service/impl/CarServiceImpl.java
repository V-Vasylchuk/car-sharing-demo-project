package com.demo.carsharing.service.impl;

import com.demo.carsharing.exception.DataProcessingException;
import com.demo.carsharing.model.Car;
import com.demo.carsharing.repository.CarRepository;
import com.demo.carsharing.service.CarService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;

    @Override
    @Transactional
    public Car createCar(Car car) {
        return carRepository.save(car);
    }

    @Override
    public List<Car> findAll() {
        return carRepository.findAll();
    }

    @Override
    public Car findById(Long id) {
        return carRepository.getReferenceById(id);
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
