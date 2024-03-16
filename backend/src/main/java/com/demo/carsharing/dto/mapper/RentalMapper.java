package com.demo.carsharing.dto.mapper;

import com.demo.carsharing.dto.request.RentalRequestDto;
import com.demo.carsharing.dto.response.RentalResponseDto;
import com.demo.carsharing.model.Rental;
import com.demo.carsharing.repository.CarRepository;
import com.demo.carsharing.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class RentalMapper implements DtoMapper<Rental, RentalRequestDto, RentalResponseDto> {

    private final CarRepository carRepository;
    private final UserRepository userRepository;

    @Override
    public Rental toModel(RentalRequestDto requestDto) {
        return new Rental()
                .setRentalDate(requestDto.getRentalDate())
                .setReturnDate(requestDto.getReturnDate())
                .setActualReturnDate(requestDto.getActualReturnDate())
                .setUser(userRepository.getReferenceById(requestDto.getUserId()))
                .setCar(carRepository.getReferenceById(requestDto.getCarId()));
    }

    @Override
    public RentalResponseDto toDto(Rental model) {
        return new RentalResponseDto()
                .setId(model.getId())
                .setRentalDate(model.getRentalDate())
                .setReturnDate(model.getReturnDate())
                .setActualReturnDate(model.getActualReturnDate())
                .setUserId(model.getUser().getId())
                .setCarId(model.getCar().getId());
    }
}
