package com.ga.YouCINEMA.service;

import com.ga.YouCINEMA.dto.request.BookingRequest;
import com.ga.YouCINEMA.dto.response.BookingResponse;
import com.ga.YouCINEMA.enums.BookingStatus;
import com.ga.YouCINEMA.exception.InformationExistException;
import com.ga.YouCINEMA.exception.InformationNotFoundException;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class BookingService {

    // One lock per seat ID — application level concurrency control
    private final ConcurrentHashMap<Long, ReentrantLock> seatLocks = new ConcurrentHashMap<>();

    private BookingRepository bookingRepository;
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    public void setBookingRepository(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Autowired
    public void setShowtimeRepository(ShowtimeRepository showtimeRepository) {
        this.showtimeRepository = showtimeRepository;
    }

    private ReentrantLock getLockForSeat(Long seatId) {
        return seatLocks.computeIfAbsent(seatId, id -> new ReentrantLock());
    }

    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        // Get logged-in user
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();

        // Get showtime
        Showtime showtime = showtimeRepository.findById(request.getShowtimeId())
                .orElseThrow(() -> new InformationNotFoundException("Showtime not found"));

        // Get and validate seats
        List<Seat> seats = new ArrayList<>();
        for (Long seatId : request.getSeatIds()) {
            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() -> new InformationNotFoundException("Seat not found: " + seatId));

            if (!seat.getCinemaHall().getId().equals(showtime.getCinemaHall().getId())) {
                throw new InformationNotFoundException("Seat " + seat.getSeatNumber() + " does not belong to this showtime's hall");
            }

            seats.add(seat);
        }

        // Acquire locks for all seats — application level
        List<ReentrantLock> acquiredLocks = new ArrayList<>();
        try {
            for (Seat seat : seats) {
                ReentrantLock lock = getLockForSeat(seat.getId());
                lock.lock();
                acquiredLocks.add(lock);
            }

            // Check if any seat is already booked for this showtime
            for (Seat seat : seats) {
                boolean alreadyBooked = bookingRepository
                        .existsByShowtimeIdAndBookedSeatsSeatIdAndStatusNot(
                                showtime.getId(),
                                seat.getId(),
                                BookingStatus.CANCELLED
                        );
                if (alreadyBooked) {
                    throw new InformationExistException(
                            "Seat " + seat.getSeatNumber() + " is already booked for this showtime"
                    );
                }
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

            // Create booking seats — @Version handles DB level concurrency
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
                throw new InformationExistException("One or more seats were just booked by another user. Please select different seats.");
            }

            bookingRepository.save(booking);
            return mapToResponse(booking);

        } finally {
            // Always release all locks
            for (ReentrantLock lock : acquiredLocks) {
                lock.unlock();
            }
        }
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
                .orElseThrow(() -> new InformationNotFoundException("Booking not found"));

        if (!booking.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only cancel your own bookings");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new InformationExistException("Booking is already cancelled");
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