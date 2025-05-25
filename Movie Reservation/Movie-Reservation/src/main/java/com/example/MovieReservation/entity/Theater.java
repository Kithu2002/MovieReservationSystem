package com.example.MovieReservation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "theaters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"seats", "showtimes"})
@ToString(exclude = {"seats", "showtimes"})
public class Theater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Integer totalSeats;

    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Seat> seats = new HashSet<>();

    @OneToMany(mappedBy = "theater",fetch = FetchType.LAZY)
    private Set<Showtime> showtimes = new HashSet<>();
}