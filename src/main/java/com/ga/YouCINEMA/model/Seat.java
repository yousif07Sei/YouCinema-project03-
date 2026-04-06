package com.ga.YouCINEMA.model;
import lombok.*;
import jakarta.persistence.*;

import com.ga.YouCINEMA.enums.SeatStatus;
import com.ga.YouCINEMA.enums.SeatType;

@Entity
@Table(name = "seats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {

    private Long id;

    private CinemaHall cinemaHall;

    private String seatNumber;

    private String row;

    private SeatType seatType;

    private SeatStatus status;

    private int Version;

}
