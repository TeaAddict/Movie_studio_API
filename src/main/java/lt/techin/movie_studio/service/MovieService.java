package lt.techin.movie_studio.service;

import lt.techin.movie_studio.model.Movie;
import lt.techin.movie_studio.repository.MovieRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
  private final MovieRepository movieRepository;

  @Autowired
  public MovieService(MovieRepository movieRepository) {
    this.movieRepository = movieRepository;
  }

  public List<Movie> getAllMovies() {
    return movieRepository.findAll();
  }

  public Optional<Movie> getMovieById(long id) {
    return movieRepository.findById(id);
  }

  public List<Movie> getAllMoviesByName(String name) {
    return movieRepository.findAllMoviesByName(name);
  }

  public Movie saveMovie(Movie movie) {
    return movieRepository.save(movie);
  }

  public boolean existsMovieById(long id) {
    return movieRepository.existsById(id);
  }

  public Optional<Movie> findMovieById(long id) {
    return movieRepository.findById(id);
  }

  public void deleteMovie(long id) {
    movieRepository.deleteById(id);
  }

  public Page<Movie> findAllMoviesPage(int page, int size) {
    Pageable pageObj = PageRequest.of(page, size);
    return movieRepository.findAll(pageObj);
  }

  public boolean existsMovieByNameAndDirector(String name, String director) {
    return movieRepository.existsByNameAndDirector(name, director);
  }
}
