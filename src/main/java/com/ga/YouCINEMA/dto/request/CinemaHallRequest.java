package com.ga.YouCINEMA.dto.request;

import com.ga.YouCINEMA.enums.HallType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CinemaHallRequest {


    @NotBlank(message = "Hall name is required")
    private String name;

    @NotNull(message = "Hall type is required")
    private HallType hallType;

    @NotNull(message = "Total seats is required")
    @Min(value = 1, message = "Total seats must be at least 1")
    private Integer totalSeats;

    @Min(value = 1, message = "Seats per row must be at least 1")
    @NotNull(message = "Seats per row is required")
    private Integer seatsPerRow;

}
