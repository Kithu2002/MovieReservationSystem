package com.example.MovieReservation.config;

import com.example.MovieReservation.entity.*;
import com.example.MovieReservation.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TheaterRepository theaterRepository;
    private final SeatRepository seatRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, RoleRepository roleRepository,
                      TheaterRepository theaterRepository, SeatRepository seatRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.theaterRepository = theaterRepository;
        this.seatRepository = seatRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create roles
        Role userRole = createRoleIfNotExists(Role.RoleName.ROLE_USER);
        Role adminRole = createRoleIfNotExists(Role.RoleName.ROLE_ADMIN);

        // Create admin user
        if (!userRepository.existsByEmail("admin@moviereservation.com")) {
            User admin = new User();
            admin.setEmail("admin@moviereservation.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.getRoles().add(adminRole);
            admin.getRoles().add(userRole);
            userRepository.save(admin);
        }

        // Create theaters and seats
        createTheatersAndSeats();
    }

    private Role createRoleIfNotExists(Role.RoleName roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(roleName);
                    return roleRepository.save(role);
                });
    }

    private void createTheatersAndSeats() {
        if (theaterRepository.count() == 0) {
            // Create Theater 1
            Theater theater1 = new Theater();
            theater1.setName("Main Theater");
            theater1.setTotalSeats(100);
            theater1 = theaterRepository.save(theater1);
            createSeatsForTheater(theater1, 10, 10);

            // Create Theater 2
            Theater theater2 = new Theater();
            theater2.setName("VIP Theater");
            theater2.setTotalSeats(50);
            theater2 = theaterRepository.save(theater2);
            createSeatsForTheater(theater2, 5, 10);
        }
    }

    private void createSeatsForTheater(Theater theater, int rows, int seatsPerRow) {
        for (int row = 0; row < rows; row++) {
            String rowLetter = String.valueOf((char) ('A' + row));
            for (int seatNum = 1; seatNum <= seatsPerRow; seatNum++) {
                Seat seat = new Seat();
                seat.setTheater(theater);
                seat.setSeatNumber(rowLetter + seatNum);
                seat.setRowLetter(rowLetter);
                seat.setSeatInRow(seatNum);

                // Set seat types
                if (row < 2) {
                    seat.setType(Seat.SeatType.VIP);
                } else if (row < 5) {
                    seat.setType(Seat.SeatType.PREMIUM);
                } else {
                    seat.setType(Seat.SeatType.REGULAR);
                }

                seatRepository.save(seat);
            }
        }
    }
}