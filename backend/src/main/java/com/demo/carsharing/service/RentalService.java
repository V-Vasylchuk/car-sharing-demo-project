package com.demo.carsharing.service;

import com.demo.carsharing.dto.request.RentalRequestDto;
import com.demo.carsharing.dto.response.RentalResponseDto;
import java.util.List;
import org.springframework.data.domain.PageRequest;

public interface RentalService {
    RentalResponseDto save(RentalRequestDto rentalRequestDto);

    RentalResponseDto getById(Long id);

    void delete(Long id);

    List<RentalResponseDto> findAllByUserId(Long userId, PageRequest pageRequest);

    List<RentalResponseDto> findAll();

    void updateActualReturnDate(Long id);

    List<RentalResponseDto> findAllByActualReturnDateAfterReturnDate();
}
