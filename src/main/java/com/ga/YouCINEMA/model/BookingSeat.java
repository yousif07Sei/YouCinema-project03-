package com.ga.YouCINEMA.model;
import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "booking_seats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingSeat {
    private Long id;

    private Booking booking;

    private Seat seat;
}
