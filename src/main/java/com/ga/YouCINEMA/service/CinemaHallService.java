package com.ga.YouCINEMA.service;

import com.ga.YouCINEMA.dto.request.CinemaHallRequest;
import com.ga.YouCINEMA.dto.response.CinemaHallResponse;
import com.ga.YouCINEMA.enums.SeatStatus;
import com.ga.YouCINEMA.enums.SeatType;
import com.ga.YouCINEMA.model.CinemaHall;
import com.ga.YouCINEMA.model.Seat;
import com.ga.YouCINEMA.repository.CinemaHallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CinemaHallService {

    private CinemaHallRepository cinemaHallRepository;

    @Autowired
    public void setCinemaHallRepository(CinemaHallRepository cinemaHallRepository) {
        this.cinemaHallRepository = cinemaHallRepository;
    }

    @Transactional
    public CinemaHallResponse createHall(CinemaHallRequest request) {
        CinemaHall hall = CinemaHall.builder()
                .name(request.getName())
                .hallType(request.getHallType())
                .totalSeats(request.getTotalSeats())
                .build();

        List<Seat> seats = generateSeats(hall, request.getTotalSeats(), request.getSeatsPerRow());
        hall.setSeats(seats);

        cinemaHallRepository.save(hall);
        return mapToResponse(hall);
    }

    public List<CinemaHallResponse> getAllHalls() {
        return cinemaHallRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CinemaHallResponse getHallById(Long id) {
        CinemaHall hall = cinemaHallRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cinema hall not found"));
        return mapToResponse(hall);
    }


    public void deleteHall(Long id) {
        if (!cinemaHallRepository.existsById(id)) {
            throw new RuntimeException("Cinema hall not found");
        }
        cinemaHallRepository.deleteById(id);
    }

    private List<Seat> generateSeats(CinemaHall hall, int totalSeats, int seatsPerRow) {
        List<Seat> seats = new ArrayList<>();
        int seatCount = 0;
        int rowIndex = 0;

        while (seatCount < totalSeats) {
            String rowLabel = String.valueOf((char) ('A' + rowIndex));
            int seatsInThisRow = Math.min(seatsPerRow, totalSeats - seatCount);

            for (int i = 1; i <= seatsInThisRow; i++) {
                Seat seat = Seat.builder()
                        .cinemaHall(hall)
                        .row(rowLabel)
                        .seatNumber(rowLabel + i)
                        .seatType(SeatType.STANDARD)
                        .status(SeatStatus.AVAILABLE)
                        .build();
                seats.add(seat);
            }

            seatCount += seatsInThisRow;
            rowIndex++;
        }

        return seats;
    }

    private CinemaHallResponse mapToResponse(CinemaHall hall) {
        return CinemaHallResponse.builder()
                .id(hall.getId())
                .name(hall.getName())
                .hallType(hall.getHallType())
                .totalSeats(hall.getTotalSeats())
                .build();
    }
}