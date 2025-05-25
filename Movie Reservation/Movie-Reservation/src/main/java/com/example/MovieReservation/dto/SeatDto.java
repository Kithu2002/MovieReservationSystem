package com.example.MovieReservation.dto;

import lombok.Data;

@Data
public class SeatDto {
    private Long id;
    private String seatNumber;
    private String type;
    private Boolean isAvailable;
    private Double price;
}