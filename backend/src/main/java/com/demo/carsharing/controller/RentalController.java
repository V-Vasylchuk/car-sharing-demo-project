package com.demo.carsharing.controller;

import com.demo.carsharing.dto.request.RentalRequestDto;
import com.demo.carsharing.dto.response.RentalResponseDto;
import com.demo.carsharing.service.CarService;
import com.demo.carsharing.service.NotificationService;
import com.demo.carsharing.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rentals")
public class RentalController {
    private final RentalService rentalService;
    private final CarService carService;
    private final NotificationService notificationService;

    @PostMapping
    @Operation(summary = "Add a new rental")
    public RentalResponseDto add(@RequestBody @Valid RentalRequestDto rentalRequestDto) {
        log.debug("Try create new Rental with MultipartFile");
        carService.decreaseInventory(rentalRequestDto.getCarId(), 1);
        RentalResponseDto rentalResponseDto = rentalService.save(rentalRequestDto);
        log.debug("New Rental was successfully created");
        notificationService.sendMessageAboutNewRental(rentalResponseDto);
        return rentalResponseDto;
    }

    @GetMapping
    @Operation(summary = "Get all rentals by userId and status")
    public List<RentalResponseDto> getByUserAndActive(
            @RequestParam("user_id")
            @Parameter(description = "user id") Long userId,
            @RequestParam("is_active")
            @Parameter(description = "active rental or not") boolean isActive,
            @RequestParam(defaultValue = "10")
            @Parameter(description = "default value is '10'") Integer count,
            @RequestParam(defaultValue = "0")
            @Parameter(description = "default value is '0'") Integer page) {
        log.debug("Try get all Rentals");
        PageRequest pageRequest = PageRequest.of(page, count);
        if (isActive) {
            return rentalService.findAllByUserId(userId, pageRequest).stream()
                    .filter(date -> date.getActualReturnDate() == null)
                    .toList();
        }
        log.debug("All Rentals was successfully got");
        return rentalService.findAllByUserId(userId, pageRequest);
    }

    @GetMapping("/")
    @Operation(summary = "Get all rentals")
    public List<RentalResponseDto> getByUser() {

        return rentalService.findAll();
    }

    @PostMapping("/{id}/return")
    @Operation(summary = "Set car return date")
    public RentalResponseDto setActualDate(
            @Parameter(description = "Rental's id to find set the date of car return")
            @PathVariable Long id) {
        log.debug("Try update Rental actual date");
        rentalService.updateActualReturnDate(id);
        RentalResponseDto rentalResponseDto = rentalService.getById(id);
        carService.increaseInventory(rentalResponseDto.getCar().getId(), 1);
        log.debug("Rental actual date was updated");
        return rentalResponseDto;
    }
}
