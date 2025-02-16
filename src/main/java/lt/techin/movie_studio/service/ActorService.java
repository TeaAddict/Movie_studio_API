package lt.techin.movie_studio.service;

import lt.techin.movie_studio.model.Actor;
import lt.techin.movie_studio.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActorService {
  private final ActorRepository actorRepository;

  @Autowired
  public ActorService(ActorRepository actorRepository) {
    this.actorRepository = actorRepository;
  }

  public Actor saveActor(Actor actor) {
    return actorRepository.save(actor);
  }

  public List<Actor> getAllActors() {
    return actorRepository.findAll();
  }

  public boolean actorExistsById(long id) {
    return actorRepository.existsById(id);
  }

  public Optional<Actor> getActorById(long id) {
    return actorRepository.findById(id);
  }

  public void deleteActorById(long id) {
    actorRepository.deleteById(id);
  }
}
