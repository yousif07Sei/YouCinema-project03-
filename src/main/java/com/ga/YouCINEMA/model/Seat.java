package com.ga.YouCINEMA.model;

import com.ga.YouCINEMA.enums.SeatStatus;
import com.ga.YouCINEMA.enums.SeatType;

public class Seat {

    private Long id;

    private CinemaHall cinemaHall;

    private String seatNumber;

    private String row;

    private SeatType seatType;

    private SeatStatus status;

    private int Version;

}
