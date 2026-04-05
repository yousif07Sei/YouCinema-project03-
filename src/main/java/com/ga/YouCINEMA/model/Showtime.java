package com.ga.YouCINEMA.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Showtime {

    private Long id;

    private  Movie movie;

    private CinemaHall cinemaHall;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private BigDecimal price;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
