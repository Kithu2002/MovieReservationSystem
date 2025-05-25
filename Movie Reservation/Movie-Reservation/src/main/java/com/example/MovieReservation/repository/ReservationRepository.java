package com.example.MovieReservation.repository;


import com.example.MovieReservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);

    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId AND r.showtime.startTime > :currentTime AND r.status = 'CONFIRMED'")
    List<Reservation> findUpcomingReservationsByUserId(@Param("userId") Long userId,
                                                       @Param("currentTime") LocalDateTime currentTime);

    List<Reservation> findByShowtimeId(Long showtimeId);

    @Query("SELECT SUM(r.totalAmount) FROM Reservation r WHERE r.status = 'CONFIRMED'")
    Double getTotalRevenue();

    @Query("SELECT SUM(r.totalAmount) FROM Reservation r WHERE r.showtime.movie.id = :movieId AND r.status = 'CONFIRMED'")
    Double getRevenueByMovieId(@Param("movieId") Long movieId);
}