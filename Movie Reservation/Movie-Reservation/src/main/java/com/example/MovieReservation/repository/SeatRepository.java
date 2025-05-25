package com.example.MovieReservation.repository;


import com.example.MovieReservation.entity.Seat;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByTheaterId(Long theaterId);
}
