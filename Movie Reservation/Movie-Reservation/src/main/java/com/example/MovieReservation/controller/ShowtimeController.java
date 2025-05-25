package com.example.MovieReservation.controller;

import com.example.MovieReservation.dto.ShowtimeDto;
import com.example.MovieReservation.service.ShowtimeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/showtimes")
public class ShowtimeController {

    private final ShowtimeService showtimeService;

    public ShowtimeController(ShowtimeService showtimeService) {
        this.showtimeService = showtimeService;
    }

    @GetMapping("/public/movie/{movieId}")
    public ResponseEntity<List<ShowtimeDto>> getShowtimesByMovie(@PathVariable Long movieId) {
        return ResponseEntity.ok(showtimeService.getShowtimesByMovieId(movieId));
    }

    @GetMapping("/public/date")
    public ResponseEntity<List<ShowtimeDto>> getShowtimesByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(showtimeService.getShowtimesByDate(date));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShowtimeDto> createShowtime(@RequestBody ShowtimeDto showtimeDto) {
        return ResponseEntity.ok(showtimeService.createShowtime(showtimeDto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShowtimeDto> updateShowtime(@PathVariable Long id,
                                                      @RequestBody ShowtimeDto showtimeDto) {
        return ResponseEntity.ok(showtimeService.updateShowtime(id, showtimeDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteShowtime(@PathVariable Long id) {
        showtimeService.deleteShowtime(id);
        return ResponseEntity.ok("Showtime deleted successfully");
    }
}
