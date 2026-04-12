package com.ga.YouCINEMA.model;
import lombok.*;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@Table(name = "showtimes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
