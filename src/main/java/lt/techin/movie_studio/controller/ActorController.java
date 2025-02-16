package lt.techin.movie_studio.controller;

import jakarta.validation.Valid;
import lt.techin.movie_studio.dto.ActorMapper;
import lt.techin.movie_studio.dto.ActorRequestDTO;
import lt.techin.movie_studio.dto.ActorResponseDTO;
import lt.techin.movie_studio.model.Actor;
import lt.techin.movie_studio.service.ActorService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ActorController {
  private final ActorService actorService;

  @Autowired
  public ActorController(ActorService actorService) {
    this.actorService = actorService;
  }

  @GetMapping("/actors")
  public ResponseEntity<List<ActorResponseDTO>> getAllActors() {
    List<Actor> actors = actorService.getAllActors();
    List<ActorResponseDTO> actorsDTO = ActorMapper.toActorResponseDTOS(actors);
    return ResponseEntity.ok().body(actorsDTO);
  }

  @PostMapping("/actors")
  public ResponseEntity<ActorResponseDTO> saveActor(@Valid @RequestBody ActorRequestDTO actorDTO) {
    Actor actor = ActorMapper.toActor(actorDTO);

    Actor savedActor = actorService.saveActor(actor);
    ActorResponseDTO savedActorDTO = ActorMapper.toActorResponseDTO((savedActor));

    return ResponseEntity.created(ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .replacePath("/actor/{id}")
                    .buildAndExpand(actor.getId())
                    .toUri())
            .body(savedActorDTO);
  }

  @PutMapping("/actors/{id}")
  public ResponseEntity<ActorResponseDTO> updateActor(@Valid @RequestBody ActorRequestDTO actorRequestDTO, @PathVariable long id) {

    Optional<Actor> actor = actorService.getActorById(id);
    if (actor.isEmpty()) {
      return ResponseEntity.notFound().build();
    }


  }

  @DeleteMapping("/actors/{id}")
  public ResponseEntity<?> deleteActor(@PathVariable long id) {

    if (!actorService.actorExistsById(id)) {
      return ResponseEntity.badRequest().body("Actor does not exist");
    }

    actorService.deleteActorById(id);

    return ResponseEntity.noContent().build();
  }
}
