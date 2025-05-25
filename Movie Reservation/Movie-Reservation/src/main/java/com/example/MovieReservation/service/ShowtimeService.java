package com.example.MovieReservation.service;

import com.example.MovieReservation.dto.ShowtimeDto;
import com.example.MovieReservation.entity.Movie;
import com.example.MovieReservation.entity.Showtime;
import com.example.MovieReservation.entity.Theater;
import com.example.MovieReservation.repository.MovieRepository;
import com.example.MovieReservation.repository.ShowtimeRepository;

import com.example.MovieReservation.repository.ReservedSeatRepository;
import com.example.MovieReservation.repository.TheaterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;
    private final ReservedSeatRepository reservedSeatRepository;

    public ShowtimeService(ShowtimeRepository showtimeRepository,
                           MovieRepository movieRepository,
                           TheaterRepository theaterRepository,
                           ReservedSeatRepository reservedSeatRepository) {
        this.showtimeRepository = showtimeRepository;
        this.movieRepository = movieRepository;
        this.theaterRepository = theaterRepository;
        this.reservedSeatRepository = reservedSeatRepository;
    }

    public ShowtimeDto createShowtime(ShowtimeDto showtimeDto) {
        Movie movie = movieRepository.findById(showtimeDto.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Theater theater = theaterRepository.findById(showtimeDto.getTheaterId())
                .orElseThrow(() -> new RuntimeException("Theater not found"));

        Showtime showtime = new Showtime();
        showtime.setMovie(movie);
        showtime.setTheater(theater);
        showtime.setStartTime(showtimeDto.getStartTime());
        showtime.setEndTime(showtimeDto.getStartTime().plusMinutes(movie.getDuration()));
        showtime.setPrice(showtimeDto.getPrice());

        showtime = showtimeRepository.save(showtime);
        return convertToDto(showtime);
    }

    public ShowtimeDto updateShowtime(Long id, ShowtimeDto showtimeDto) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Showtime not found"));

        if (showtimeDto.getStartTime() != null) {
            showtime.setStartTime(showtimeDto.getStartTime());
            showtime.setEndTime(showtimeDto.getStartTime().plusMinutes(showtime.getMovie().getDuration()));
        }

        if (showtimeDto.getPrice() != null) {
            showtime.setPrice(showtimeDto.getPrice());
        }

        showtime = showtimeRepository.save(showtime);
        return convertToDto(showtime);
    }

    public void deleteShowtime(Long id) {
        if (!showtimeRepository.existsById(id)) {
            throw new RuntimeException("Showtime not found");
        }
        showtimeRepository.deleteById(id);
    }

    public List<ShowtimeDto> getShowtimesByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        return showtimeRepository.findByDateRange(startOfDay, endOfDay).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ShowtimeDto> getShowtimesByMovieId(Long movieId) {
        return showtimeRepository.findUpcomingShowtimesByMovieId(movieId, LocalDateTime.now()).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ShowtimeDto convertToDto(Showtime showtime) {
        ShowtimeDto dto = new ShowtimeDto();
        dto.setId(showtime.getId());
        dto.setMovieId(showtime.getMovie().getId());
        dto.setMovieTitle(showtime.getMovie().getTitle());
        dto.setTheaterId(showtime.getTheater().getId());
        dto.setTheaterName(showtime.getTheater().getName());
        dto.setStartTime(showtime.getStartTime());
        dto.setEndTime(showtime.getEndTime());
        dto.setPrice(showtime.getPrice());

        // Calculate available seats - use a separate query instead of accessing collections
        int totalSeats = showtime.getTheater().getTotalSeats();
        int reservedSeats = reservedSeatRepository.findByShowtimeId(showtime.getId()).size();
        dto.setAvailableSeats(totalSeats - reservedSeats);

        return dto;
    }
}
