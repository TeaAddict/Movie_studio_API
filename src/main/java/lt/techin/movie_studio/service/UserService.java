package lt.techin.movie_studio.service;

import lt.techin.movie_studio.model.User;
import lt.techin.movie_studio.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User saveUser(User user) {
    return userRepository.save(user);
  }

  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public Optional<User> findUserById(long id) {
    return userRepository.findById(id);
  }

  public List<User> findAllUsers() {
    return userRepository.findAll();
  }

  public void deleteUser(long id) {
    userRepository.deleteById(id);
  }

  public boolean existsUserById(long id) {
    return userRepository.existsById(id);
  }
}
