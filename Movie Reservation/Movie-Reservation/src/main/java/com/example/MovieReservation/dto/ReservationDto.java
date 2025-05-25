package com.example.MovieReservation.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;




@Data
public class ReservationDto {
    private Long id;
    private Long userId;
    private String userName;
    private Long showtimeId;
    private String movieTitle;
    private String theaterName;
    private LocalDateTime showtime;
    private List<String> seatNumbers;
    private LocalDateTime reservationTime;
    private Double totalAmount;
    private String status;
}