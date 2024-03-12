package com.demo.carsharing.service.impl;

import com.demo.carsharing.model.Rental;
import com.demo.carsharing.repository.RentalRepository;
import com.demo.carsharing.service.RentalService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;

    @Override
    @Transactional
    public Rental save(Rental rental) {
        return rentalRepository.save(rental);
    }

    @Override
    public Rental getById(Long id) {
        return rentalRepository.getReferenceById(id);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        rentalRepository.deleteById(id);
    }

    @Override
    public List<Rental> findAllByUserId(Long userId, PageRequest pageRequest) {
        return rentalRepository.findAllByUserId(userId, pageRequest);
    }

    @Override
    @Transactional
    public void updateActualReturnDate(Long id) {
        Rental rental = getById(id);
        if (rental.getActualReturnDate() == null) {
            rental.setActualReturnDate(LocalDateTime.now());
            save(rental);
        } else {
            throw new RuntimeException("Car is already returned.");
        }
    }

    @Override
    public List<Rental> findAllByActualReturnDateAfterReturnDate() {
        return rentalRepository.findAllByActualReturnDateAfterReturnDate();
    }

    @Override
    public List<Rental> findAll() {
        return rentalRepository.findAll();
    }
}
