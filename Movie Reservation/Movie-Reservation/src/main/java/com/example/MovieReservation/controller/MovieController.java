package com.example.MovieReservation.controller;

import com.example.MovieReservation.dto.MovieDto;
import com.example.MovieReservation.dto.ShowtimeDto;
import com.example.MovieReservation.service.MovieService;
import com.example.MovieReservation.service.ShowtimeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;
    private final ShowtimeService showtimeService;

    public MovieController(MovieService movieService, ShowtimeService showtimeService) {
        this.movieService = movieService;
        this.showtimeService = showtimeService;
    }

    @GetMapping("/public")
    public ResponseEntity<List<MovieDto>> getAllActiveMovies() {
        return ResponseEntity.ok(movieService.getAllActiveMovies());
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    @GetMapping("/public/genre/{genre}")
    public ResponseEntity<List<MovieDto>> getMoviesByGenre(@PathVariable String genre) {
        return ResponseEntity.ok(movieService.getMoviesByGenre(genre));
    }

    @GetMapping("/public/{movieId}/showtimes")
    public ResponseEntity<List<ShowtimeDto>> getMovieShowtimes(@PathVariable Long movieId) {
        return ResponseEntity.ok(showtimeService.getShowtimesByMovieId(movieId));
    }

    @GetMapping("/public/showtimes/date")
    public ResponseEntity<List<ShowtimeDto>> getShowtimesByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(showtimeService.getShowtimesByDate(date));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieDto> createMovie(@RequestBody MovieDto movieDto) {
        return ResponseEntity.ok(movieService.createMovie(movieDto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieDto> updateMovie(@PathVariable Long id, @RequestBody MovieDto movieDto) {
        return ResponseEntity.ok(movieService.updateMovie(id, movieDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok("Movie deleted successfully");
    }
}