package com.demo.carsharing.service;

import com.demo.carsharing.model.Car;
import java.util.List;

public interface CarService {
    Car createCar(Car car);

    List<Car> findAll();

    Car findById(Long id);

    Car update(Car car);

    void deleteById(Long id);

    void decreaseInventory(Long carId, int number);

    void increaseInventory(Long carId, int number);
}
