package com.ga.YouCINEMA.repository;

import com.ga.YouCINEMA.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByCinemaHallId(Long hallId);

    @Query("""
        SELECT s FROM Seat s
        WHERE s.cinemaHall.id = (
            SELECT st.cinemaHall.id FROM Showtime st WHERE st.id = :showtimeId
        )
        AND s.id NOT IN (
            SELECT bs.seat.id FROM BookingSeat bs
            WHERE bs.booking.showtime.id = :showtimeId
            AND bs.booking.status = 'CONFIRMED'
        )
    """)
    List<Seat> findAvailableSeatsByShowtime(@Param("showtimeId") Long showtimeId);
}
