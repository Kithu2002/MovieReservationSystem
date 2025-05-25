package com.example.MovieReservation.controller;

import com.example.MovieReservation.dto.ReservationDto;
import com.example.MovieReservation.service.UserService;
import com.example.MovieReservation.dto.SeatDto;
import com.example.MovieReservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final UserService userService;

    public ReservationController(ReservationService reservationService, UserService userService) {
        this.reservationService = reservationService;
        this.userService = userService;
    }

    @GetMapping("/seats/{showtimeId}")
    public ResponseEntity<List<SeatDto>> getAvailableSeats(@PathVariable Long showtimeId) {
        return ResponseEntity.ok(reservationService.getAvailableSeats(showtimeId));
    }

    @PostMapping
    public ResponseEntity<ReservationDto> createReservation(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Map<String, Object> request) {
        Long userId = userService.getUserIdByEmail(userDetails.getUsername());
        Long showtimeId = Long.valueOf(request.get("showtimeId").toString());
        List<Long> seatIds = ((List<?>) request.get("seatIds")).stream()
                .map(id -> Long.valueOf(id.toString()))
                .toList();

        return ResponseEntity.ok(reservationService.createReservation(userId, showtimeId, seatIds));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ReservationDto>> getMyReservations(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userService.getUserIdByEmail(userDetails.getUsername());
        return ResponseEntity.ok(reservationService.getUserReservations(userId));
    }

    @GetMapping("/my/upcoming")
    public ResponseEntity<List<ReservationDto>> getMyUpcomingReservations(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userService.getUserIdByEmail(userDetails.getUsername());
        return ResponseEntity.ok(reservationService.getUpcomingUserReservations(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelReservation(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userService.getUserIdByEmail(userDetails.getUsername());
        reservationService.cancelReservation(id, userId);
        return ResponseEntity.ok("Reservation cancelled successfully");
    }
}