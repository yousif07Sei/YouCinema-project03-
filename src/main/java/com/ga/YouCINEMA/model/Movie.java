package com.ga.YouCINEMA.model;
import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {
    private Long id ;

    private String title;

    private String description;

    private int duration;

    private String genre;

    private String language;

    private String posterUrl;

    private LocalDate releaseDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
