package com.demo.carsharing.service.impl;

import com.demo.carsharing.dto.mapper.DtoMapper;
import com.demo.carsharing.dto.request.RentalRequestDto;
import com.demo.carsharing.dto.response.RentalResponseDto;
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

    private final DtoMapper<Rental, RentalRequestDto, RentalResponseDto> mapper;
    private final RentalRepository rentalRepository;

    @Override
    @Transactional
    public RentalResponseDto save(RentalRequestDto rentalRequestDto) {
        Rental rental = mapper.toModel(rentalRequestDto);
        rentalRepository.save(rental);
        return mapper.toDto(rental);
    }

    @Override
    public RentalResponseDto getById(Long id) {
        Rental rental = rentalRepository.getReferenceById(id);
        return mapper.toDto(rental);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        rentalRepository.deleteById(id);
    }

    @Override
    public List<RentalResponseDto> findAllByUserId(Long userId, PageRequest pageRequest) {
        List<Rental> rentalList = rentalRepository.findAllByUserId(userId, pageRequest);
        return rentalList.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void updateActualReturnDate(Long id) {
        Rental rental = rentalRepository.getReferenceById(id);
        if (rental.getActualReturnDate() == null) {
            rental.setActualReturnDate(LocalDateTime.now());
            rentalRepository.save(rental);
        } else {
            throw new RuntimeException("Car is already returned.");
        }
    }

    @Override
    public List<RentalResponseDto> findAllByActualReturnDateAfterReturnDate() {
        List<Rental> rentalList = rentalRepository.findAllByActualReturnDateAfterReturnDate();
        return rentalList.stream()
                .map(mapper::toDto)
                .toList();
    }
}
