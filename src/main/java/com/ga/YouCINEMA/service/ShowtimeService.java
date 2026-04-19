package com.ga.YouCINEMA.service;

import com.ga.YouCINEMA.dto.request.ShowtimeRequest;
import com.ga.YouCINEMA.dto.response.ShowtimeResponse;
import com.ga.YouCINEMA.model.CinemaHall;
import com.ga.YouCINEMA.model.Movie;
import com.ga.YouCINEMA.model.Showtime;
import com.ga.YouCINEMA.repository.CinemaHallRepository;
import com.ga.YouCINEMA.repository.MovieRepository;
import com.ga.YouCINEMA.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowtimeService {

    private ShowtimeRepository showtimeRepository;
    private MovieRepository movieRepository;
    private CinemaHallRepository cinemaHallRepository;

    @Autowired
    public void setShowtimeRepository(ShowtimeRepository showtimeRepository) {
        this.showtimeRepository = showtimeRepository;
    }

    @Autowired
    public void setMovieRepository(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Autowired
    public void setCinemaHallRepository(CinemaHallRepository cinemaHallRepository) {
        this.cinemaHallRepository = cinemaHallRepository;
    }

    public ShowtimeResponse createShowtime(ShowtimeRequest request) {
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        CinemaHall hall = cinemaHallRepository.findById(request.getHallId())
                .orElseThrow(() -> new RuntimeException("Cinema hall not found"));

        Showtime showtime = Showtime.builder()
                .movie(movie)
                .cinemaHall(hall)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .price(request.getPrice())
                .build();

        showtimeRepository.save(showtime);
        return mapToResponse(showtime);
    }

    public List<ShowtimeResponse> getAllShowtimes() {
        return showtimeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ShowtimeResponse getShowtimeById(Long id) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Showtime not found"));
        return mapToResponse(showtime);
    }

    public void deleteShowtime(Long id) {
        if (!showtimeRepository.existsById(id)) {
            throw new RuntimeException("Showtime not found");
        }
        showtimeRepository.deleteById(id);
    }

    private ShowtimeResponse mapToResponse(Showtime showtime) {
        return ShowtimeResponse.builder()
                .id(showtime.getId())
                .movieId(showtime.getMovie().getId())
                .movieTitle(showtime.getMovie().getTitle())
                .hallId(showtime.getCinemaHall().getId())
                .hallName(showtime.getCinemaHall().getName())
                .startTime(showtime.getStartTime())
                .endTime(showtime.getEndTime())
                .price(showtime.getPrice())
                .build();
    }
}
