package com.ga.YouCINEMA.dto.response;

import com.ga.YouCINEMA.enums.BookingStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {

    private Long id;
    private Long userId;
    private String userEmail;
    private Long showtimeId;
    private String movieTitle;
    private List<String> seatNumbers;
    private BigDecimal totalPrice;
    private BookingStatus status;
    private LocalDateTime bookedAt;

}
