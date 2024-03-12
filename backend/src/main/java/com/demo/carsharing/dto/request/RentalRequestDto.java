package com.demo.carsharing.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RentalRequestDto {
    private static final String TIME_EXAMPLE = "2023-06-05T12:34:56";
    private static final String POSITIVE_NUMBER_EXAMPLE = "1";

    @NotNull
    @Schema(example = TIME_EXAMPLE)
    private LocalDateTime rentalDate;
    @NotNull
    @Schema(example = TIME_EXAMPLE)
    private LocalDateTime returnDate;
    private LocalDateTime actualReturnDate;
    @Positive
    @Schema(example = POSITIVE_NUMBER_EXAMPLE)
    private Long carId;
    @Positive
    @Schema(example = POSITIVE_NUMBER_EXAMPLE)
    private Long userId;
}
