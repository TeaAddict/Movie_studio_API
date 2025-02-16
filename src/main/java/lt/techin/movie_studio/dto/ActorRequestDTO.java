package lt.techin.movie_studio.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ActorRequestDTO(
        @NotNull
        @Size(min = 2, max = 50, message = "Actor name size should be between 2-50 characters")
        String firstName,

        @NotNull
        @Size(min = 2, max = 50, message = "Actor name size should be between 2-50 characters")
        String lastName,

        @Min(value = 18, message = "Minimum age is 18")
        @Max(value = 110, message = "Maximum age is 110")
        int age) {
}
