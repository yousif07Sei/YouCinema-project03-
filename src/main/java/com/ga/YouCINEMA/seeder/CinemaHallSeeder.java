package com.ga.YouCINEMA.seeder;

import com.ga.YouCINEMA.enums.HallType;
import com.ga.YouCINEMA.enums.SeatStatus;
import com.ga.YouCINEMA.enums.SeatType;
import com.ga.YouCINEMA.model.CinemaHall;
import com.ga.YouCINEMA.model.Seat;
import com.ga.YouCINEMA.repository.CinemaHallRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CinemaHallSeeder {

    private static final Logger logger = LoggerFactory.getLogger(CinemaHallSeeder.class);

    @Autowired
    private CinemaHallRepository cinemaHallRepository;

    public void seed() {
        logger.info("🎬 Seeding cinema halls...");

        if (cinemaHallRepository.count() > 0) {
            logger.info("⏭️  Cinema halls already exist (count: {}), skipping", cinemaHallRepository.count());
            return;
        }

        // Hall 1 - STANDARD 50 seats
        CinemaHall standard = CinemaHall.builder()
                .name("Hall 1 - Standard")
                .hallType(HallType.STANDARD)
                .totalSeats(50)
                .build();
        standard.setSeats(generateSeats(standard, 50, 10));
        cinemaHallRepository.save(standard);

        // Hall 2 - IMAX 40 seats
        CinemaHall imax = CinemaHall.builder()
                .name("Hall 2 - IMAX")
                .hallType(HallType.IMAX)
                .totalSeats(40)
                .build();
        imax.setSeats(generateSeats(imax, 40, 10));
        cinemaHallRepository.save(imax);

        // Hall 3 - VIP 20 seats
        CinemaHall vip = CinemaHall.builder()
                .name("Hall 3 - VIP")
                .hallType(HallType.VIP)
                .totalSeats(20)
                .build();
        vip.setSeats(generateSeats(vip, 20, 5));
        cinemaHallRepository.save(vip);

        logger.info("✅ Created 3 cinema halls");
        logger.info("   Hall 1 - Standard (50 seats, 10 per row)");
        logger.info("   Hall 2 - IMAX     (40 seats, 10 per row)");
        logger.info("   Hall 3 - VIP      (20 seats, 5 per row)");
    }

    private List<Seat> generateSeats(CinemaHall hall, int totalSeats, int seatsPerRow) {
        List<Seat> seats = new ArrayList<>();
        int seatCount = 0;
        int rowIndex = 0;

        while (seatCount < totalSeats) {
            String rowLabel = String.valueOf((char) ('A' + rowIndex));
            int seatsInThisRow = Math.min(seatsPerRow, totalSeats - seatCount);

            for (int i = 1; i <= seatsInThisRow; i++) {
                seats.add(Seat.builder()
                        .cinemaHall(hall)
                        .row(rowLabel)
                        .seatNumber(rowLabel + i)
                        .seatType(SeatType.STANDARD)
                        .status(SeatStatus.AVAILABLE)
                        .build());
            }

            seatCount += seatsInThisRow;
            rowIndex++;
        }

        return seats;
    }
}
