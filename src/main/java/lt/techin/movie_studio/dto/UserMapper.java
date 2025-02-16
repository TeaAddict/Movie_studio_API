package lt.techin.movie_studio.dto;

import lt.techin.movie_studio.model.Role;
import lt.techin.movie_studio.model.User;

import java.util.List;

public class UserMapper {

//  public static UserResponseDTO toUserResponseDTO(UserRequestDTO userRequestDTO) {
//    return new UserResponseDTO(userRequestDTO.username(), userRequestDTO.roles());
//  }

  public static User toUser(UserRequestDTO userRequestDTO) {
    return new User(userRequestDTO.username(), userRequestDTO.password(), List.of());
  }

  public static UserResponseDTO toUserResponseDTO(User user) {
    return new UserResponseDTO(user.getId(), user.getUsername(), user.getRoles().stream().map(Role::getName).toList());
  }
}
