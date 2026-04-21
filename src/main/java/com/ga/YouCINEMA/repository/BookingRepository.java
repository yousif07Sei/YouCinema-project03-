package com.ga.YouCINEMA.repository;

import com.ga.YouCINEMA.enums.BookingStatus;
import com.ga.YouCINEMA.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);

    boolean existsByShowtimeIdAndBookedSeatsSeatIdAndStatusNot(
            Long showtimeId,
            Long seatId,
            BookingStatus status
    );
}
