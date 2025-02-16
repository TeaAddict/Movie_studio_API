package lt.techin.movie_studio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.techin.movie_studio.model.Movie;
import lt.techin.movie_studio.security.SecurityConfig;
import lt.techin.movie_studio.service.MovieService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MovieController.class)
@Import({SecurityConfig.class})
public class MovieControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private MovieService movieService;

  List<Movie> movies = List.of(
          new Movie("Titan", "Jerry", "Best movie ever"),
          new Movie("Planetx", "Thomas")
  );

  // Happy path
  @Test
  @WithMockUser(authorities = "SCOPE_ROLE_USER")
  void getMovies_whenFindAll_thenReturnAlland200() throws Exception {

    // GIVEN
    BDDMockito.given(movieService.getAllMovies()).willReturn(movies);

    // WHEN
    mockMvc.perform(MockMvcRequestBuilders.get("/api/movies"))
            // THEN
            .andExpect(status().isOk())
            .andExpect(jsonPath("length()").value(2))
            .andExpect(jsonPath("[0].id").exists())
            .andExpect(jsonPath("[0].name").value("Titan"))
            .andExpect(jsonPath("[0].director").value("Jerry"))
            .andExpect(jsonPath("[0].description").value("Best movie ever"))

            .andExpect(jsonPath("[1].id").exists())
            .andExpect(jsonPath("[1].name").value(movies.get(1).getName()))
            .andExpect(jsonPath("[1].director").value(movies.get(1).getDirector()))
            .andExpect(jsonPath("[1].description").isEmpty());

    Mockito.verify(movieService, times(1)).getAllMovies();
  }

  @Test
  @WithMockUser(authorities = "SCOPE_ROLE_USER")
  void getMovies_whenFindAllIsEmptyAuthenticated_thenRespond200() throws Exception {
    // GIVEN
    BDDMockito.given(movieService.getAllMovies()).willReturn(List.of());

    // WHEN
    mockMvc.perform(MockMvcRequestBuilders.get("/api/movies"))
            // THEN
            .andExpect(status().isOk())
            .andExpect(jsonPath("length()").value(0));

    Mockito.verify(movieService, times(1)).getAllMovies();
  }

  // Unhappy path
  @Test
  void getMovies_whenFindAllUnauthenticated_thenRespond401() throws Exception {
    // GIVEN
    BDDMockito.given(movieService.getAllMovies()).willReturn(movies);

    // WHEN
    mockMvc.perform(MockMvcRequestBuilders.get("/api/movies"))
            // THEN
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$").doesNotExist());

    Mockito.verify(movieService, times(0)).getAllMovies();
  }


  // Happy path
  @Test
  @WithMockUser(authorities = "SCOPE_ROLE_USER")
  void getMovie_whenExistingIdAuthenticated_thenRespond200() throws Exception {
    // GIVEN
    long id = 0L;
    Movie movie = new Movie("Avatar", "Cameron");

    BDDMockito.given(movieService.getMovieById(id)).willReturn(Optional.of(movie));

    // WHEN
    mockMvc.perform(MockMvcRequestBuilders.get("/api/movies/" + id))
            // THEN
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value("Avatar"))
            .andExpect(jsonPath("$.director").value("Cameron"));

    Mockito.verify(movieService, times(1)).getMovieById(id);
  }

  // Unhappy path
  @Test
  @WithMockUser(authorities = "SCOPE_ROLE_USER")
  void getMovie_whenNotExistingIdAuthenticated_thenRespond404() throws Exception {
    // GIVEN
    long id = 5;
    BDDMockito.given(movieService.getMovieById(id)).willReturn(Optional.empty());

    // WHEN
    mockMvc.perform(MockMvcRequestBuilders.get("/api/movies/" + id))
            // THEN
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$").doesNotExist());

    Mockito.verify(movieService, times(1)).getMovieById(id);
  }

  @Test
  void getMovie_whenUnauthenticated_thenRespond401() throws Exception {
    // GIVEN
    long id = 0;
    Movie movie = new Movie("Avatar", "Cameron");
    BDDMockito.given(movieService.findMovieById(id)).willReturn(Optional.of(movie));

    // WHEN
    mockMvc.perform(MockMvcRequestBuilders.get("/api/movie/" + id))
            // THEN
            .andExpect(status().isUnauthorized());
  }

  // Happy path
  @Test
  @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
  void addMovie_whenAllValidatedAuthorized_thenRespond201() throws Exception {
    // GIVEN
    Movie movie = new Movie("Avatar", "Cameron");
    BDDMockito.given(movieService.saveMovie(movie)).willReturn(movie);

    // WHEN
    mockMvc.perform(MockMvcRequestBuilders.post("/api/movies")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(movie)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists());
  }
}
