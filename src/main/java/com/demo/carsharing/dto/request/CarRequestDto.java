package com.demo.carsharing.dto.request;

import com.demo.carsharing.model.Car;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarRequestDto {
    private static final String NOT_EMPTY_MESSAGE = "Field can`t be empty!";

    private Long id;
    @Schema(example = "Toyota")
    @NotEmpty(message = NOT_EMPTY_MESSAGE)
    private String brand;
    @Schema(example = "Camry")
    @NotEmpty(message = NOT_EMPTY_MESSAGE)
    private String model;
    @Schema(example = "SEDAN")
    @NotNull
    @Enumerated(EnumType.STRING)
    private Car.Type type;
    @Schema(example = "10")
    @NotNull
    @PositiveOrZero
    private Integer inventory;
    @Schema(example = "50,12")
    @NotNull
    @Positive
    private BigDecimal dailyFee;
    private String bucketName;
    private String keyName;
    private String presignedUrl;
}
