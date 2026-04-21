package com.ga.YouCINEMA.seeder;


import com.ga.YouCINEMA.model.CinemaHall;
import com.ga.YouCINEMA.model.Movie;
import com.ga.YouCINEMA.model.Showtime;
import com.ga.YouCINEMA.repository.CinemaHallRepository;
import com.ga.YouCINEMA.repository.MovieRepository;
import com.ga.YouCINEMA.repository.ShowtimeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ShowtimeSeeder {

    private static final Logger logger = LoggerFactory.getLogger(ShowtimeSeeder.class);

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CinemaHallRepository cinemaHallRepository;

    public void seed() {
        logger.info("🎬 Seeding showtimes...");

        if (showtimeRepository.count() > 0) {
            logger.info("⏭️  Showtimes already exist (count: {}), skipping", showtimeRepository.count());
            return;
        }

        Movie fastFurious1 = movieRepository.findByTitle("The Fast and the Furious");
        Movie fastFive = movieRepository.findByTitle("Fast Five");
        Movie furious7 = movieRepository.findByTitle("Furious 7");
        Movie f9 = movieRepository.findByTitle("F9");

        CinemaHall standard = cinemaHallRepository.findByName("Hall 1 - Standard");
        CinemaHall imax = cinemaHallRepository.findByName("Hall 2 - IMAX");
        CinemaHall vip = cinemaHallRepository.findByName("Hall 3 - VIP");

        showtimeRepository.saveAll(List.of(
                // The Fast and the Furious - Standard
                Showtime.builder()
                        .movie(fastFurious1)
                        .cinemaHall(standard)
                        .startTime(LocalDateTime.of(2026, 5, 1, 10, 0))
                        .endTime(LocalDateTime.of(2026, 5, 1, 11, 46))
                        .price(BigDecimal.valueOf(5.50))
                        .build(),

                // Fast Five - IMAX
                Showtime.builder()
                        .movie(fastFive)
                        .cinemaHall(imax)
                        .startTime(LocalDateTime.of(2026, 5, 1, 13, 0))
                        .endTime(LocalDateTime.of(2026, 5, 1, 15, 10))
                        .price(BigDecimal.valueOf(8.00))
                        .build(),

                // Furious 7 - VIP
                Showtime.builder()
                        .movie(furious7)
                        .cinemaHall(vip)
                        .startTime(LocalDateTime.of(2026, 5, 1, 16, 0))
                        .endTime(LocalDateTime.of(2026, 5, 1, 18, 17))
                        .price(BigDecimal.valueOf(12.00))
                        .build(),

                // F9 - Standard
                Showtime.builder()
                        .movie(f9)
                        .cinemaHall(standard)
                        .startTime(LocalDateTime.of(2026, 5, 1, 19, 0))
                        .endTime(LocalDateTime.of(2026, 5, 1, 21, 23))
                        .price(BigDecimal.valueOf(5.50))
                        .build(),

                // Fast Five - Standard (evening)
                Showtime.builder()
                        .movie(fastFive)
                        .cinemaHall(standard)
                        .startTime(LocalDateTime.of(2026, 5, 2, 10, 0))
                        .endTime(LocalDateTime.of(2026, 5, 2, 12, 10))
                        .price(BigDecimal.valueOf(5.50))
                        .build()
        ));

        logger.info("✅ Created 5 showtimes");
        logger.info("   The Fast and the Furious - Standard  10:00");
        logger.info("   Fast Five                - IMAX      13:00");
        logger.info("   Furious 7                - VIP       16:00");
        logger.info("   F9                       - Standard  19:00");
        logger.info("   Fast Five                - Standard  10:00 (May 2)");
    }

}
