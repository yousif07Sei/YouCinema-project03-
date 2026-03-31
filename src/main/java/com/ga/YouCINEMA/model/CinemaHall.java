package com.ga.YouCINEMA.model;

import java.time.LocalDateTime;

public class CinemaHall {

    private Long id;

    private String name;

    private String hallType;

    private int totalSeats;

    private List<Seat> seats;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
