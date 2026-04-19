package com.ga.YouCINEMA.dto.response;

import com.ga.YouCINEMA.enums.HallType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CinemaHallResponse {

    private Long id;
    private String name;
    private HallType hallType;
    private int totalSeats;

}
