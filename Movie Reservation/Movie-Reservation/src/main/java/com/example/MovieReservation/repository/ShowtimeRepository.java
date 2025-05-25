package com.example.MovieReservation.repository;


import com.example.MovieReservation.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    @Query("SELECT s FROM Showtime s WHERE s.startTime BETWEEN :startDate AND :endDate")
    List<Showtime> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                   @Param("endDate") LocalDateTime endDate);

    List<Showtime> findByMovieId(Long movieId);

    @Query("SELECT s FROM Showtime s WHERE s.movie.id = :movieId AND s.startTime > :currentTime")
    List<Showtime> findUpcomingShowtimesByMovieId(@Param("movieId") Long movieId,
                                                  @Param("currentTime") LocalDateTime currentTime);



    List<Showtime> findByTheaterId(Long theaterId);

}