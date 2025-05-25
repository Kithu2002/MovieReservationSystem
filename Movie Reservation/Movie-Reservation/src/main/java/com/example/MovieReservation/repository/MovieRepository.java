package com.example.MovieReservation.repository;


import com.example.MovieReservation.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByIsActiveTrue();

    @Query("SELECT m FROM Movie m JOIN m.genres g WHERE g.name = :genreName AND m.isActive = true")
    List<Movie> findByGenreName(String genreName);
}
