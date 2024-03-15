package com.demo.carsharing.service;

import com.demo.carsharing.model.Car;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface CarService {
    Car createCar(Car car, MultipartFile file);

    List<Car> findAll();

    Car findById(Long id);

    Car update(Car car);

    void deleteById(Long id);

    void decreaseInventory(Long carId, int number);

    void increaseInventory(Long carId, int number);
}
