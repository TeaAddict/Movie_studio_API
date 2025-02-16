package lt.techin.movie_studio.controller;

import jakarta.validation.Valid;
import lt.techin.movie_studio.dto.MovieMapper;
import lt.techin.movie_studio.dto.MovieRequestDTO;
import lt.techin.movie_studio.dto.MovieResponseDTO;
import lt.techin.movie_studio.model.Movie;
import lt.techin.movie_studio.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MovieController {
  private final MovieService movieService;

  @Autowired
  public MovieController(MovieService movieService) {
    this.movieService = movieService;
  }

  @GetMapping("/movies")
  public ResponseEntity<List<MovieResponseDTO>> getAllMovies() {
    List<MovieResponseDTO> movieResponseDTOS = MovieMapper.toMovieResponseDTOS(movieService.getAllMovies());
    return ResponseEntity.ok(movieResponseDTOS);
  }

  @GetMapping("/movies/{id}")
  public ResponseEntity<MovieResponseDTO> getMovie(@PathVariable long id) {
    Optional<Movie> movie = movieService.getMovieById(id);
    if (movie.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    MovieResponseDTO movieResponseDTO = MovieMapper.toMovieResponseDTO(movie.get());
    return ResponseEntity.ok(movieResponseDTO);
  }

  @GetMapping("/movies/search/by-name")
  public ResponseEntity<?> getMovieByName(@RequestParam String name) {
    if (name == null || name.trim().isEmpty()) {
      return ResponseEntity.badRequest().body("Movie name missing");
    }

    List<Movie> movie = movieService.getAllMoviesByName(name);

    if (movie == null) {
      String responseBody = "Movie by name " + name + " does not exist";
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    return ResponseEntity.ok().body(movie);
  }

  @PostMapping("/movies")
  public ResponseEntity<MovieResponseDTO> saveMovie(@Valid @RequestBody MovieRequestDTO movieDTO) {

    boolean exists = movieService.existsMovieByNameAndDirector(movieDTO.name(), movieDTO.director());
    if (!exists) {
      return ResponseEntity.notFound().build();
    }

    Movie movie = MovieMapper.toMovie(movieDTO);
    Movie savedMovie = movieService.saveMovie(movie);

    MovieResponseDTO movieResponseDTO = MovieMapper.toMovieResponseDTO(movie);

    return ResponseEntity.created(
                    ServletUriComponentsBuilder.fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(savedMovie.getId())
                            .toUri())
            .body(movieResponseDTO);
  }

  @PutMapping("/movies/{id}")
  public ResponseEntity<?> updateMovie(@Valid @RequestBody MovieRequestDTO movieDTO, @PathVariable long id) {

    if (movieService.existsMovieById(id)) {
      Movie movieFromDb = movieService.findMovieById(id).get();

      movieFromDb.setName(movieDTO.name());
      movieFromDb.setDirector(movieDTO.director());
      movieFromDb.setDescription(movieDTO.description());
      movieFromDb.setScreenings(movieDTO.screenings());
      movieFromDb.setActors(movieDTO.actors());

      return ResponseEntity.ok(movieService.saveMovie(movieFromDb));
    }

    Movie movie = MovieMapper.toMovie(movieDTO);

    Movie savedMovie = movieService.saveMovie(movie);

    return ResponseEntity.created(
                    ServletUriComponentsBuilder.fromCurrentRequest()
                            .replacePath("/api/movies/{id}")
                            .buildAndExpand(savedMovie.getId())
                            .toUri())
            .body(movieDTO);
  }

  @DeleteMapping("/movies/{id}")
  public ResponseEntity<?> deleteMovie(@PathVariable long id) {
    movieService.deleteMovie(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/movies/pagination")
  public ResponseEntity<?> getMoviesPagination(@RequestParam int page,
                                               @RequestParam int size) {
    Page<Movie> pageMovie = movieService.findAllMoviesPage(page, size);

    Page<MovieResponseDTO> movieDTOpage = MovieMapper.pageMoviesToMovieDTO(pageMovie);

    return ResponseEntity.ok(movieDTOpage);
  }
}
