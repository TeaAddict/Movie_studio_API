package lt.techin.movie_studio.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ScreeningDTO(
        @NotNull
        @Size(min = 2, max = 50, message = "Name size should be between 2 and 50")
        String theaterName,

        @NotNull
        LocalDateTime screeningTime) {
}
