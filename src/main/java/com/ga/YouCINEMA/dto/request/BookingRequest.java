package com.ga.YouCINEMA.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequest {

    @NotNull(message = "Showtime ID is required")
    private Long showtimeId;

    @NotEmpty(message = "At least one seat is required")
    private List<Long> seatIds;
}
