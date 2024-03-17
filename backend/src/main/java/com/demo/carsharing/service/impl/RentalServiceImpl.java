package com.demo.carsharing.service.impl;

import com.demo.carsharing.dto.mapper.DtoMapper;
import com.demo.carsharing.dto.request.RentalRequestDto;
import com.demo.carsharing.dto.response.RentalResponseDto;
import com.demo.carsharing.model.Rental;
import com.demo.carsharing.repository.RentalRepository;
import com.demo.carsharing.service.RentalService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {

    private final DtoMapper<Rental, RentalRequestDto, RentalResponseDto> mapper;
    private final RentalRepository rentalRepository;

    @Override
    @Transactional
    public RentalResponseDto save(RentalRequestDto rentalRequestDto) {
        log.debug("Try create new Rental and save to DB");
        Rental rental = mapper.toModel(rentalRequestDto);
        rentalRepository.save(rental);
        log.debug("New Rental {} was created and saved to DB", rental);
        return mapper.toDto(rental);
    }

    @Override
    public RentalResponseDto getById(Long id) {
        log.debug("Try get Rental by id {} from DB", id);
        Rental rental = rentalRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Car not found by ID " + id));
        log.debug("Rental by id {} was successfully got from DB", id);
        return mapper.toDto(rental);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Try delete Rental by id {} from DB", id);
        try {
            rentalRepository.deleteById(id);
        } catch (EmptyResultDataAccessException exception) {
            throw new EntityNotFoundException("Rental by id " + id
                    + " does not exist or has been deleted");
        }
        log.debug("Rental by id {} was successfully deleted from DB", id);
    }

    @Override
    public List<RentalResponseDto> findAllByUserId(Long userId, PageRequest pageRequest) {
        log.debug("Try get all Rentals from DB by User id {} ", userId);
        List<Rental> rentalList = rentalRepository.findAllByUserId(userId, pageRequest);
        if (rentalList.isEmpty()) {
            throw new EntityNotFoundException("Rentals not fount  by User id {} " + userId);
        }
        log.debug("All Rentals was successfully got from DB");
        return rentalList.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<RentalResponseDto> findAll() {
        List<Rental> rentalList = rentalRepository.findAll();
        return rentalList.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void updateActualReturnDate(Long id) {
        log.debug("Try update actual Return date by Rental id {} in DB", id);
        Rental rental = rentalRepository.getReferenceById(id);
        if (rental.getActualReturnDate() == null) {
            rental.setActualReturnDate(LocalDateTime.now());
            rentalRepository.save(rental);
        } else {
            throw new RuntimeException("Car is already returned.");
        }
        log.debug("Actual Return date by Rental id {} was successfully updated in DB", id);
    }

    @Override
    public List<RentalResponseDto> findAllByActualReturnDateAfterReturnDate() {
        log.debug("Try get all Rentals by actual return date after return date from DB");
        List<Rental> rentalList = rentalRepository.findAllByActualReturnDateAfterReturnDate();
        if (rentalList.isEmpty()) {
            throw new EntityNotFoundException("Rentals not fount!");
        }
        log.debug("All Rentals by actual return date after return date "
                + "was successfully got from DB");
        return rentalList.stream()
                .map(mapper::toDto)
                .toList();
    }
}
