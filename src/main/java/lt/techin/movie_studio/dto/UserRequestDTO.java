package lt.techin.movie_studio.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotNull
        @Size(min = 8, max = 100)
        String username,

        @NotNull
        @Size(min = 8, max = 100)
        String password
) {
  @Override
  public String username() {
    return username;
  }

  @Override
  public String password() {
    return password;
  }
}
