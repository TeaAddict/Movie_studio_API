package lt.techin.movie_studio.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lt.techin.movie_studio.model.Actor;
import lt.techin.movie_studio.model.Screening;

import java.util.List;

public record MovieRequestDTO(
        @NotNull
        @Size(min = 2, max = 50)
        String name,

        @Pattern(regexp = "^[A-Z][a-z]*$")
        String director,

        String description,
        List<Screening> screenings,
        List<Actor> actors
) {
}
