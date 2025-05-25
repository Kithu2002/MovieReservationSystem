package com.example.MovieReservation.service;

import com.example.MovieReservation.dto.MovieDto;
import com.example.MovieReservation.entity.Genre;
import com.example.MovieReservation.entity.Movie;
import com.example.MovieReservation.entity.Movie;
import com.example.MovieReservation.repository.GenreRepository;
import com.example.MovieReservation.repository.MovieRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;

    public MovieService(MovieRepository movieRepository, GenreRepository genreRepository) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
    }

    public MovieDto createMovie(MovieDto movieDto) {
        Movie movie = new Movie();
        movie.setTitle(movieDto.getTitle());
        movie.setDescription(movieDto.getDescription());
        movie.setPosterImageUrl(movieDto.getPosterImageUrl());
        movie.setDuration(movieDto.getDuration());

        if (movieDto.getGenres() != null) {
            for (String genreName : movieDto.getGenres()) {
                Genre genre = genreRepository.findByName(genreName)
                        .orElseGet(() -> {
                            Genre newGenre = new Genre();
                            newGenre.setName(genreName);
                            return genreRepository.save(newGenre);
                        });
                movie.getGenres().add(genre);
            }
        }

        movie = movieRepository.save(movie);
        return convertToDto(movie);
    }

    public MovieDto updateMovie(Long id, MovieDto movieDto) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        movie.setTitle(movieDto.getTitle());
        movie.setDescription(movieDto.getDescription());
        movie.setPosterImageUrl(movieDto.getPosterImageUrl());
        movie.setDuration(movieDto.getDuration());
        movie.setIsActive(movieDto.getIsActive());


        Set<Genre> newGenres = new HashSet<>();
        if (movieDto.getGenres() != null) {
            for (String genreName : movieDto.getGenres()) {
                Genre genre = genreRepository.findByName(genreName)
                        .orElseGet(() -> {
                            Genre newGenre = new Genre();
                            newGenre.setName(genreName);
                            return genreRepository.save(newGenre);
                        });
                newGenres.add(genre);
            }
        }
        movie.setGenres(newGenres);

        movie = movieRepository.save(movie);
        return convertToDto(movie);
    }

    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        movie.setIsActive(false);
        movieRepository.save(movie);
    }

    public List<MovieDto> getAllActiveMovies() {
        return movieRepository.findByIsActiveTrue().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public MovieDto getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        return convertToDto(movie);
    }

    public List<MovieDto> getMoviesByGenre(String genreName) {
        return movieRepository.findByGenreName(genreName).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private MovieDto convertToDto(Movie movie) {
        MovieDto dto = new MovieDto();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setDescription(movie.getDescription());
        dto.setPosterImageUrl(movie.getPosterImageUrl());
        dto.setDuration(movie.getDuration());
        dto.setGenres(movie.getGenres().stream()
                .map(Genre::getName)
                .collect(Collectors.toSet()));
        dto.setIsActive(movie.getIsActive());
        return dto;
    }
}
