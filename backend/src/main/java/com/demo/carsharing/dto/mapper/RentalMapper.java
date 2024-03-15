package com.demo.carsharing.dto.mapper;

import com.demo.carsharing.dto.request.RentalRequestDto;
import com.demo.carsharing.dto.response.RentalResponseDto;
import com.demo.carsharing.model.Rental;
import com.demo.carsharing.service.CarService;
import com.demo.carsharing.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class RentalMapper implements DtoMapper<Rental, RentalRequestDto, RentalResponseDto> {
    private final CarService carService;
    private final UserService userService;

    @Override
    public Rental toModel(RentalRequestDto requestDto) {
        return new Rental()
                .setRentalDate(requestDto.getRentalDate())
                .setReturnDate(requestDto.getReturnDate())
                .setActualReturnDate(requestDto.getActualReturnDate())
                .setUser(userService.findById(requestDto.getUserId()))
                .setCar(carService.findById(requestDto.getCarId()));
    }

    @Override
    public RentalResponseDto toDto(Rental model) {
        return new RentalResponseDto()
                .setId(model.getId())
                .setRentalDate(model.getRentalDate())
                .setReturnDate(model.getReturnDate())
                .setActualReturnDate(model.getActualReturnDate())
                .setUserId(model.getUser().getId())
                .setUserFirstName(model.getUser().getFirstName())
                .setCarBrand(model.getCar().getBrand())
                .setCarId(model.getCar().getId());
    }
}
