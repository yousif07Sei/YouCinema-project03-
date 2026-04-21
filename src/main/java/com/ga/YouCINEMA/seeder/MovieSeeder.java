package com.ga.YouCINEMA.seeder;
import com.ga.YouCINEMA.model.Movie;
import com.ga.YouCINEMA.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;


@Component
public class MovieSeeder {
    private static final Logger logger = LoggerFactory.getLogger(MovieSeeder.class);

    @Autowired
    private MovieRepository movieRepository;

    public void seed() {
        logger.info("🎬 Seeding movies...");

        if (movieRepository.count() > 0) {
            logger.info("⏭️  Movies already exist (count: {}), skipping", movieRepository.count());
            return;
        }

        movieRepository.saveAll(List.of(
                Movie.builder()
                        .title("The Fast and the Furious")
                        .description("An undercover cop joins a gang of street racers suspected of theft.")
                        .genre("Action")
                        .language("English")
                        .duration(106)
                        .releaseDate(LocalDate.of(2001, 6, 22))
                        .build(),

                Movie.builder()
                        .title("Fast Five")
                        .description("Dom and his crew plan a heist to steal $100 million from a ruthless drug lord in Rio.")
                        .genre("Action")
                        .language("English")
                        .duration(130)
                        .releaseDate(LocalDate.of(2011, 4, 29))
                        .build(),

                Movie.builder()
                        .title("Furious 7")
                        .description("Deckard Shaw seeks revenge against Dom and his crew for his comatose brother.")
                        .genre("Action")
                        .language("English")
                        .duration(137)
                        .releaseDate(LocalDate.of(2015, 4, 3))
                        .build(),

                Movie.builder()
                        .title("F9")
                        .description("Dom and his crew face Dom's younger brother Jakob, a skilled assassin working with their enemy.")
                        .genre("Action")
                        .language("English")
                        .duration(143)
                        .releaseDate(LocalDate.of(2021, 6, 25))
                        .build()
        ));

        logger.info("✅ Created 4 Fast & Furious movies");
        logger.info("   The Fast and the Furious, Fast Five, Furious 7, F9");
    }
}
