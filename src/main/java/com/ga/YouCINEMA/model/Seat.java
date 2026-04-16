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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hall_id", nullable = false)
    private CinemaHall cinemaHall;

    @Column(nullable = false)
    private String seatNumber;

    @Column(nullable = false)
    private String row;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatType seatType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus status;

    @Version
    @Column(nullable = false)
    private int Version;

}
