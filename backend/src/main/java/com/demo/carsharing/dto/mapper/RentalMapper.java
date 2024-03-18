package com.demo.carsharing.dto.mapper;

import com.demo.carsharing.dto.request.RentalRequestDto;
import com.demo.carsharing.dto.response.CarResponseDto;
import com.demo.carsharing.dto.response.RentalResponseDto;
import com.demo.carsharing.dto.response.UserResponseDto;
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
    private final CarMapper carMapper;
    private final UserMapper userMapper;

    @Override
    public Rental toModel(RentalRequestDto requestDto) {
        return toModel(requestDto, new Rental());
    }

    @Override
    public Rental toModel(RentalRequestDto requestDto, Rental model) {
        return model
                .setRentalDate(requestDto.getRentalDate())
                .setReturnDate(requestDto.getReturnDate())
                .setActualReturnDate(requestDto.getActualReturnDate())
                .setUser(userRepository.getReferenceById(requestDto.getUserId()))
                .setCar(carRepository.getReferenceById(requestDto.getCarId()));
    }

    @Override
    public RentalResponseDto toDto(Rental model) {
        CarResponseDto car = carMapper.toDto(model.getCar());
        UserResponseDto user = userMapper.toDto(model.getUser());

        return new RentalResponseDto()
                .setId(model.getId())
                .setRentalDate(model.getRentalDate())
                .setReturnDate(model.getReturnDate())
                .setActualReturnDate(model.getActualReturnDate())
                .setUser(user)
                .setCar(car);
    }
}
