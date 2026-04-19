package com.ga.YouCINEMA.service;

import com.ga.YouCINEMA.dto.request.BookingRequest;
import com.ga.YouCINEMA.dto.response.BookingResponse;
import com.ga.YouCINEMA.enums.BookingStatus;
import com.ga.YouCINEMA.model.*;
import com.ga.YouCINEMA.repository.BookingRepository;
import com.ga.YouCINEMA.repository.SeatRepository;
import com.ga.YouCINEMA.repository.ShowtimeRepository;
import com.ga.YouCINEMA.security.MyUserDetails;
import jakarta.persistence.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    private BookingRepository bookingRepository;
    private ShowtimeRepository showtimeRepository;
    private SeatRepository seatRepository;


    @Autowired
    public void setBookingRepository(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Autowired
    public void setShowtimeRepository(ShowtimeRepository showtimeRepository) {
        this.showtimeRepository = showtimeRepository;
    }

    @Autowired
    public void setSeatRepository(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        // Get logged-in user
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();

        // Get showtime
        Showtime showtime = showtimeRepository.findById(request.getShowtimeId())
                .orElseThrow(() -> new RuntimeException("Showtime not found"));

        // Get and validate seats
        List<Seat> seats = new ArrayList<>();
        for (Long seatId : request.getSeatIds()) {
            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() -> new RuntimeException("Seat not found: " + seatId));

            // Check seat belongs to the showtime's hall
            if (!seat.getCinemaHall().getId().equals(showtime.getCinemaHall().getId())) {
                throw new RuntimeException("Seat " + seat.getSeatNumber() + " does not belong to this showtime's hall");
            }

            seats.add(seat);
        }

        // Calculate total price
        BigDecimal totalPrice = showtime.getPrice()
                .multiply(BigDecimal.valueOf(seats.size()));

        // Create booking
        Booking booking = Booking.builder()
                .user(user)
                .showtime(showtime)
                .totalPrice(totalPrice)
                .status(BookingStatus.CONFIRMED)
                .bookedSeats(new ArrayList<>())
                .build();

        // Create booking seats — @Version on Seat handles concurrency
        try {
            for (Seat seat : seats) {
                BookingSeat bookingSeat = BookingSeat.builder()
                        .booking(booking)
                        .seat(seat)
                        .build();
                booking.getBookedSeats().add(bookingSeat);
                seatRepository.save(seat); // triggers version check
            }
        } catch (OptimisticLockException e) {
            throw new RuntimeException("One or more seats were just booked by another user. Please select different seats.");
        }

        bookingRepository.save(booking);
        return mapToResponse(booking);
    }

    public List<BookingResponse> getMyBookings() {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();

        return bookingRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public BookingResponse cancelBooking(Long id) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only cancel your own bookings");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking is already cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
        return mapToResponse(booking);
    }

    private BookingResponse mapToResponse(Booking booking) {
        List<String> seatNumbers = booking.getBookedSeats()
                .stream()
                .map(bs -> bs.getSeat().getSeatNumber())
                .toList();

        return BookingResponse.builder()
                .id(booking.getId())
                .userId(booking.getUser().getId())
                .userEmail(booking.getUser().getEmail())
                .showtimeId(booking.getShowtime().getId())
                .movieTitle(booking.getShowtime().getMovie().getTitle())
                .seatNumbers(seatNumbers)
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus())
                .bookedAt(booking.getBookedAt())
                .build();
    }



}
