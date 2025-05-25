package com.example.MovieReservation.service;

import com.example.MovieReservation.dto.ReservationDto;
import com.example.MovieReservation.dto.SeatDto;
import com.example.MovieReservation.entity.*;
import com.example.MovieReservation.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;
    private final ReservedSeatRepository reservedSeatRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ShowtimeRepository showtimeRepository,
                              SeatRepository seatRepository,
                              ReservedSeatRepository reservedSeatRepository,
                              UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.showtimeRepository = showtimeRepository;
        this.seatRepository = seatRepository;
        this.reservedSeatRepository = reservedSeatRepository;
        this.userRepository = userRepository;
    }

    public List<SeatDto> getAvailableSeats(Long showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new RuntimeException("Showtime not found"));

        List<Long> reservedSeatIds = reservedSeatRepository.findReservedSeatIdsByShowtimeId(showtimeId);
        List<Seat> allSeats = seatRepository.findByTheaterId(showtime.getTheater().getId());

        return allSeats.stream()
                .map(seat -> {
                    SeatDto dto = new SeatDto();
                    dto.setId(seat.getId());
                    dto.setSeatNumber(seat.getSeatNumber());
                    dto.setType(seat.getType().toString());
                    dto.setIsAvailable(!reservedSeatIds.contains(seat.getId()));
                    dto.setPrice(calculateSeatPrice(showtime.getPrice(), seat.getType()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public ReservationDto createReservation(Long userId, Long showtimeId, List<Long> seatIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new RuntimeException("Showtime not found"));

        // Check if showtime has already started
        if (showtime.getStartTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cannot book seats for past showtimes");
        }

        // Check if seats are available
        List<Long> reservedSeatIds = reservedSeatRepository.findReservedSeatIdsByShowtimeId(showtimeId);
        for (Long seatId : seatIds) {
            if (reservedSeatIds.contains(seatId)) {
                throw new RuntimeException("One or more selected seats are already reserved");
            }
        }

        // Create reservation
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setShowtime(showtime);
        reservation.setReservationTime(LocalDateTime.now());
        reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);

        double totalAmount = 0;

        // Save reservation first
        reservation = reservationRepository.save(reservation);

        // Create reserved seats
        for (Long seatId : seatIds) {
            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() -> new RuntimeException("Seat not found"));

            ReservedSeat reservedSeat = new ReservedSeat();
            reservedSeat.setReservation(reservation);
            reservedSeat.setSeat(seat);
            reservedSeat.setShowtime(showtime);
            reservedSeatRepository.save(reservedSeat);

            totalAmount += calculateSeatPrice(showtime.getPrice(), seat.getType());
        }

        // Update total amount
        reservation.setTotalAmount(totalAmount);
        reservation = reservationRepository.save(reservation);

        return convertToDto(reservation);
    }

    public void cancelReservation(Long reservationId, Long userId) {
        if (userId == null) {
            throw new RuntimeException("User ID cannot be null");
        }

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        // Check if user exists and IDs match
        if (reservation.getUser() == null) {
            throw new RuntimeException("Reservation has no associated user");
        }

        Long reservationUserId = reservation.getUser().getId();
        if (!userId.equals(reservationUserId)) {
            throw new RuntimeException("You can only cancel your own reservations");
        }

        if (reservation.getShowtime().getStartTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cannot cancel past reservations");
        }

        reservation.setStatus(Reservation.ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }

    public List<ReservationDto> getUserReservations(Long userId) {
        return reservationRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ReservationDto> getUpcomingUserReservations(Long userId) {
        return reservationRepository.findUpcomingReservationsByUserId(userId, LocalDateTime.now()).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private double calculateSeatPrice(double basePrice, Seat.SeatType seatType) {
        return switch (seatType) {
            case PREMIUM -> basePrice * 1.5;
            case VIP -> basePrice * 2.0;
            default -> basePrice;
        };
    }

    private ReservationDto convertToDto(Reservation reservation) {
        ReservationDto dto = new ReservationDto();
        dto.setId(reservation.getId());
        dto.setUserId(reservation.getUser().getId());
        dto.setUserName(reservation.getUser().getFirstName() + " " + reservation.getUser().getLastName());
        dto.setShowtimeId(reservation.getShowtime().getId());
        dto.setMovieTitle(reservation.getShowtime().getMovie().getTitle());
        dto.setTheaterName(reservation.getShowtime().getTheater().getName());
        dto.setShowtime(reservation.getShowtime().getStartTime());
        dto.setSeatNumbers(reservation.getReservedSeats().stream()
                .map(rs -> rs.getSeat().getSeatNumber())
                .collect(Collectors.toList()));
        dto.setReservationTime(reservation.getReservationTime());
        dto.setTotalAmount(reservation.getTotalAmount());
        dto.setStatus(reservation.getStatus().toString());
        return dto;
    }
}