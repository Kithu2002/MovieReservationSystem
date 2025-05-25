package com.example.MovieReservation.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"theater"})
@ToString(exclude = {"theater"})
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    @Column(nullable = false)
    private String seatNumber; // e.g., "A1", "B5"

    @Column(nullable = false)
    private String rowLetter;

    @Column(nullable = false)
    private Integer seatInRow;

    @Enumerated(EnumType.STRING)
    private SeatType type = SeatType.REGULAR;

    public enum SeatType {
        REGULAR, PREMIUM, VIP
    }
}