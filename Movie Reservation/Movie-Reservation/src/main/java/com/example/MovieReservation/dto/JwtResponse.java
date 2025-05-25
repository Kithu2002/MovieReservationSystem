package com.example.MovieReservation.dto;

import lombok.Data;
import java.util.List;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String email;
    private List<String> roles;
}
