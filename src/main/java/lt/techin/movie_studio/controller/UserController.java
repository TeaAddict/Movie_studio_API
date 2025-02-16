package lt.techin.movie_studio.controller;

import jakarta.validation.Valid;
import lt.techin.movie_studio.dto.UserMapper;
import lt.techin.movie_studio.dto.UserRequestDTO;
import lt.techin.movie_studio.dto.UserResponseDTO;
import lt.techin.movie_studio.model.Role;
import lt.techin.movie_studio.model.User;
import lt.techin.movie_studio.service.RoleService;
import lt.techin.movie_studio.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final RoleService roleService;

  public UserController(UserService userService, PasswordEncoder passwordEncoder, RoleService roleService) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
    this.roleService = roleService;
  }

  @GetMapping("/users/{id}")
  public ResponseEntity<?> getUser(@PathVariable long id, Authentication authentication) {

    boolean isAdmin = (authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(a -> a.equalsIgnoreCase("SCOPE_ROLE_ADMIN")));

    if (!isAdmin) {
      if (((User) authentication.getPrincipal()).getId() != id) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Can't update other users");
      }
    }

    Optional<UserResponseDTO> userResponseDTO = userService.findUserById(id)
            .map(UserMapper::toUserResponseDTO);

    if (userResponseDTO.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(userResponseDTO.get());
  }

  @GetMapping("/users")
  public ResponseEntity<List<UserResponseDTO>> getUsers() {
    final List<User> users = userService.findAllUsers();

    if (users.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    final List<UserResponseDTO> usersResponseDTO = users.stream()
            .map(UserMapper::toUserResponseDTO)
            .toList();

    return ResponseEntity.ok(usersResponseDTO);
  }

  @PostMapping("/users")
  public ResponseEntity<?> saveUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
    Optional<User> userInDB = userService.findByUsername(userRequestDTO.username());
    if (userInDB.isPresent()) {
      return ResponseEntity.badRequest().body("User already exists");
    }

    User user = UserMapper.toUser(userRequestDTO);
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    Role role = roleService.ensureRoleExists("ROLE_USER");
    user.setRoles(List.of(role));

    User savedUser = userService.saveUser(user);
    UserResponseDTO userResponseDTO = UserMapper.toUserResponseDTO(savedUser);

    return ResponseEntity.created(
                    ServletUriComponentsBuilder.fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(userResponseDTO.id())
                            .toUri())
            .body(userResponseDTO);
  }

  @PutMapping("/users/{id}")
  public ResponseEntity<?> updateUser(@PathVariable long id,
                                      @Valid @RequestBody UserRequestDTO userRequestDTO,
                                      Authentication authentication) {

    boolean isAdmin = (authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(a -> a.equalsIgnoreCase("SCOPE_ROLE_ADMIN")));

    if (!isAdmin) {
      if (((User) authentication.getPrincipal()).getId() != id) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Can't update other users");
      }
    }

    Optional<User> user = userService.findUserById(id);
    if (user.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    user.get().setPassword(passwordEncoder.encode(userRequestDTO.password()));

    return ResponseEntity.ok(UserMapper.toUserResponseDTO(user.get()));
  }

  @DeleteMapping("/users/{id}")
  public ResponseEntity<?> deleteUser(@PathVariable long id, Authentication authentication) {

    boolean isAdmin = (authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(a -> a.equalsIgnoreCase("SCOPE_ROLE_ADMIN")));

    if (!isAdmin) {
      if (((User) authentication.getPrincipal()).getId() != id) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Can't update other users");
      }
    }

    Optional<User> user = userService.findUserById(id);
    if (user.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    userService.deleteUser(id);

    return ResponseEntity.noContent().build();
  }
}
