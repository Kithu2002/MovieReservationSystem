package com.example.MovieReservation.service;
import com.example.MovieReservation.dto.ReportDto;
import com.example.MovieReservation.entity.*;
import com.example.MovieReservation.repository.MovieRepository;
import com.example.MovieReservation.repository.ReservationRepository;
import com.example.MovieReservation.repository.ShowtimeRepository;
import com.example.MovieReservation.repository.TheaterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ReportService {

    private final ReservationRepository reservationRepository;
    private final MovieRepository movieRepository;
    private final ShowtimeRepository showtimeRepository;
    private final TheaterRepository theaterRepository;

    public ReportService(ReservationRepository reservationRepository,
                         MovieRepository movieRepository,
                         ShowtimeRepository showtimeRepository,
                         TheaterRepository theaterRepository) {
        this.reservationRepository = reservationRepository;
        this.movieRepository = movieRepository;
        this.showtimeRepository = showtimeRepository;
        this.theaterRepository = theaterRepository;
    }

    public Map<String, Object> getRevenueReport() {
        Map<String, Object> report = new HashMap<>();

        // Total revenue
        Double totalRevenue = reservationRepository.getTotalRevenue();
        report.put("totalRevenue", totalRevenue != null ? totalRevenue : 0.0);

        // Revenue by movie
        List<Movie> movies = movieRepository.findAll();
        Map<String, Double> revenueByMovie = new HashMap<>();

        for (Movie movie : movies) {
            Double movieRevenue = reservationRepository.getRevenueByMovieId(movie.getId());
            revenueByMovie.put(movie.getTitle(), movieRevenue != null ? movieRevenue : 0.0);
        }
        report.put("revenueByMovie", revenueByMovie);

        // Total reservations
        long totalReservations = reservationRepository.count();
        report.put("totalReservations", totalReservations);

        return report;
    }

    public ReportDto getMovieReport(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        ReportDto report = new ReportDto();
        report.setMovieTitle(movie.getTitle());

        // Get all showtimes for the movie
        List<Showtime> showtimes = showtimeRepository.findByMovieId(movieId);
        report.setTotalShowtimes(showtimes.size());

        // Calculate total capacity and occupancy
        int totalCapacity = 0;
        int totalReservedSeats = 0;
        double totalRevenue = 0.0;

        for (Showtime showtime : showtimes) {
            totalCapacity += showtime.getTheater().getTotalSeats();
            List<Reservation> reservations = reservationRepository.findByShowtimeId(showtime.getId());

            for (Reservation reservation : reservations) {
                if (reservation.getStatus() == Reservation.ReservationStatus.CONFIRMED) {
                    totalReservedSeats += reservation.getReservedSeats().size();
                    totalRevenue += reservation.getTotalAmount();
                }
            }
        }

        report.setTotalCapacity(totalCapacity);
        report.setTotalReservedSeats(totalReservedSeats);
        report.setOccupancyRate(totalCapacity > 0 ?
                (double) totalReservedSeats / totalCapacity * 100 : 0);
        report.setTotalRevenue(totalRevenue);

        return report;
    }

    public Map<String, Object> getOccupancyReport() {
        Map<String, Object> report = new HashMap<>();

        // Overall occupancy
        List<Theater> theaters = theaterRepository.findAll();
        Map<String, Map<String, Object>> theaterOccupancy = new HashMap<>();

        for (Theater theater : theaters) {
            Map<String, Object> theaterData = new HashMap<>();
            List<Showtime> showtimes = showtimeRepository.findByTheaterId(theater.getId());

            int totalSeatsAvailable = theater.getTotalSeats() * showtimes.size();
            int totalSeatsReserved = 0;

            for (Showtime showtime : showtimes) {
                List<Reservation> reservations = reservationRepository.findByShowtimeId(showtime.getId());
                for (Reservation reservation : reservations) {
                    if (reservation.getStatus() == Reservation.ReservationStatus.CONFIRMED) {
                        totalSeatsReserved += reservation.getReservedSeats().size();
                    }
                }
            }

            theaterData.put("totalSeats", theater.getTotalSeats());
            theaterData.put("totalShowtimes", showtimes.size());
            theaterData.put("totalCapacity", totalSeatsAvailable);
            theaterData.put("totalReserved", totalSeatsReserved);
            theaterData.put("occupancyRate", totalSeatsAvailable > 0 ?
                    (double) totalSeatsReserved / totalSeatsAvailable * 100 : 0);

            theaterOccupancy.put(theater.getName(), theaterData);
        }

        report.put("theaterOccupancy", theaterOccupancy);

        return report;
    }
}