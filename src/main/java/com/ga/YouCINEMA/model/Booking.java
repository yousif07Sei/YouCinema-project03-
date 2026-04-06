package com.ga.YouCINEMA.model;
import lombok.*;
import jakarta.persistence.*;

import com.ga.YouCINEMA.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    private Long id;

    private User user;

    private Showtime showtime;

    private List<BookingSeat> bookedSeats;

    private BigDecimal totalPrice;

    private BookingStatus status;

    private LocalDateTime bookedAt;
}
