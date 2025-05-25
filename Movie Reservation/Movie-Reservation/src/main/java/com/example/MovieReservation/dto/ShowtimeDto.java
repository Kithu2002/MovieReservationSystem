package com.example.MovieReservation.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ShowtimeDto {
    private Long id;
    private Long movieId;
    private String movieTitle;
    private Long theaterId;
    private String theaterName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double price;
    private Integer availableSeats;
}
