package com.ga.YouCINEMA.dto.response;

import com.ga.YouCINEMA.enums.SeatStatus;
import com.ga.YouCINEMA.enums.SeatType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatResponse {

    private Long id;
    private String seatNumber;
    private String row;
    private SeatType seatType;
    private SeatStatus status;
}
