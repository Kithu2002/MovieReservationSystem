package com.example.MovieReservation.controller;

import com.example.MovieReservation.dto.ReportDto;
import com.example.MovieReservation.dto.ShowtimeDto;
import com.example.MovieReservation.service.ReportService;
import com.example.MovieReservation.service.UserService;
import com.example.MovieReservation.service.ShowtimeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final ReportService reportService;

    public AdminController(UserService userService, ReportService reportService) {
        this.userService = userService;
        this.reportService = reportService;
    }

    @PostMapping("/users/{userId}/promote")
    public ResponseEntity<String> promoteUserToAdmin(@PathVariable Long userId) {
        userService.promoteToAdmin(userId);
        return ResponseEntity.ok("User promoted to admin successfully");
    }

    @GetMapping("/reports/revenue")
    public ResponseEntity<Map<String, Object>> getRevenueReport() {
        return ResponseEntity.ok(reportService.getRevenueReport());
    }

    @GetMapping("/reports/movie/{movieId}")
    public ResponseEntity<ReportDto> getMovieReport(@PathVariable Long movieId) {
        return ResponseEntity.ok(reportService.getMovieReport(movieId));
    }

    @GetMapping("/reports/occupancy")
    public ResponseEntity<Map<String, Object>> getOccupancyReport() {
        return ResponseEntity.ok(reportService.getOccupancyReport());
    }
}