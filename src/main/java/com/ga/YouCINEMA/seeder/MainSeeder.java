package com.ga.YouCINEMA.seeder;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MainSeeder {

    private static final Logger logger = LoggerFactory.getLogger(MainSeeder.class);

    @Autowired
    private UserSeeder userSeeder;

    @Autowired
    private MovieSeeder movieSeeder;

    @Autowired
    private CinemaHallSeeder cinemaHallSeeder;

    @Autowired
    private ShowtimeSeeder showtimeSeeder;

    @PostConstruct
    public void seed() {
        logger.info("🎬 ================================");
        logger.info("🎬  YouCINEMA Data Seeding Start  ");
        logger.info("🎬 ================================");

        userSeeder.seed();
        movieSeeder.seed();
        cinemaHallSeeder.seed();
        showtimeSeeder.seed();

        logger.info("🎬 ================================");
        logger.info("🎬  YouCINEMA Data Seeding Done!  ");
        logger.info("🎬 ================================");
    }
}
