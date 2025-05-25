package com.example.MovieReservation.dto;

import lombok.Data;

@Data
public class ReportDto {
    private String movieTitle;
    private Integer totalShowtimes;
    private Integer totalCapacity;
    private Integer totalReservedSeats;
    private Double occupancyRate;
    private Double totalRevenue;
}