package com.example.MovieReservation.repository;

 import com.example.MovieReservation.entity.ReservedSeat;
 import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReservedSeatRepository extends JpaRepository<ReservedSeat, Long> {
    @Query("SELECT rs FROM ReservedSeat rs WHERE rs.showtime.id = :showtimeId AND rs.reservation.status = 'CONFIRMED'")
    List<ReservedSeat> findByShowtimeId(@Param("showtimeId") Long showtimeId);

    @Query("SELECT rs.seat.id FROM ReservedSeat rs WHERE rs.showtime.id = :showtimeId AND rs.reservation.status = 'CONFIRMED'")
    List<Long> findReservedSeatIdsByShowtimeId(@Param("showtimeId") Long showtimeId);
}
