package com.example.MovieReservation.dto;

import lombok.Data;
import java.util.Set;

@Data
public class MovieDto {
    private Long id;
    private String title;
    private String description;
    private String posterImageUrl;
    private Integer duration;
    private Set<String> genres;
    private Boolean isActive;
}