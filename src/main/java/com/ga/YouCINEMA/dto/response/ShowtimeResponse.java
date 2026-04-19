package com.ga.YouCINEMA.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowtimeResponse {

    private Long id;
    private Long movieId;
    private String movieTitle;
    private Long hallId;
    private String hallName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal price;
}
