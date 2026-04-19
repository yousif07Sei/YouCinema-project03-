package com.ga.YouCINEMA.dto.request;

import com.ga.YouCINEMA.enums.SeatType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateSeatTypeRequest {

    @NotNull(message = "Seat type is required")
    private SeatType seatType;
}
