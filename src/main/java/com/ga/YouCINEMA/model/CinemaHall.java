package com.ga.YouCINEMA.model;
import lombok.*;
import jakarta.persistence.*;


import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "cinema_halls")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CinemaHall {

    private Long id;

    private String name;

    private String hallType;

    private int totalSeats;

    private List<Seat> seats;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
