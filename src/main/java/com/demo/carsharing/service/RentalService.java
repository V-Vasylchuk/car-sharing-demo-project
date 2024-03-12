package com.demo.carsharing.service;

import com.demo.carsharing.model.Rental;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.PageRequest;

public interface RentalService {
    Rental save(Rental rental);

    Rental getById(Long id);

    void delete(Long id);

    List<Rental> findAllByUserId(Long userId, PageRequest pageRequest);

    void updateActualReturnDate(Long id);

    List<Rental> findAllByActualReturnDateAfterReturnDate();
    List<Rental> findAll();
}
