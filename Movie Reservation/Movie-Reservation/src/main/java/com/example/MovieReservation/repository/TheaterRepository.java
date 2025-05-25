package com.example.MovieReservation.repository;


import com.example.MovieReservation.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
}