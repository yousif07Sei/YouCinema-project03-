package com.ga.YouCINEMA.service;

import com.ga.YouCINEMA.dto.request.UpdateSeatTypeRequest;
import com.ga.YouCINEMA.dto.response.SeatResponse;
import com.ga.YouCINEMA.model.Seat;
import com.ga.YouCINEMA.repository.CinemaHallRepository;
import com.ga.YouCINEMA.repository.SeatRepository;
import com.ga.YouCINEMA.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ga.YouCINEMA.exception.InformationNotFoundException;

import java.util.List;


@Service
public class SeatService {

    private SeatRepository seatRepository;
    private CinemaHallRepository cinemaHallRepository;
    private ShowtimeRepository showtimeRepository;

    @Autowired
    public void setSeatRepository(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    @Autowired
    public void setCinemaHallRepository(CinemaHallRepository cinemaHallRepository) {
        this.cinemaHallRepository = cinemaHallRepository;
    }

    @Autowired
    public void setShowtimeRepository(ShowtimeRepository showtimeRepository) {
        this.showtimeRepository = showtimeRepository;
    }


    public List<SeatResponse> getSeatsByHall(Long hallId) {
        if (!cinemaHallRepository.existsById(hallId)) {
            throw new InformationNotFoundException("Cinema hall not found");
        }
        return seatRepository.findByCinemaHallId(hallId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<SeatResponse> getAvailableSeatsByShowtime(Long showtimeId) {
        if (!showtimeRepository.existsById(showtimeId)) {
            throw new InformationNotFoundException("Showtime not found");
        }
        return seatRepository.findAvailableSeatsByShowtime(showtimeId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    public SeatResponse updateSeatType(Long id, UpdateSeatTypeRequest request) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new InformationNotFoundException("Seat not found"));
        seat.setSeatType(request.getSeatType());
        seatRepository.save(seat);
        return mapToResponse(seat);
    }


    private SeatResponse mapToResponse(Seat seat) {
        return SeatResponse.builder()
                .id(seat.getId())
                .seatNumber(seat.getSeatNumber())
                .row(seat.getRow())
                .seatType(seat.getSeatType())
                .status(seat.getStatus())
                .build();
    }
}