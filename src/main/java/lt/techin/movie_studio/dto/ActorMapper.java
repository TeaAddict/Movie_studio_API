package lt.techin.movie_studio.dto;

import lt.techin.movie_studio.model.Actor;

import java.util.List;

public class ActorMapper {

  public static List<ActorResponseDTO> toActorResponseDTOS(List<Actor> actorList) {
    return actorList.stream().map(actor ->
            new ActorResponseDTO(actor.getId(), actor.getFirstName(), actor.getLastName(), actor.getAge())).toList();
  }

  public static ActorResponseDTO toActorResponseDTO(Actor actor) {
    return new ActorResponseDTO(actor.getId(), actor.getFirstName(), actor.getLastName(), actor.getAge());
  }

  public static Actor toActor(ActorRequestDTO actorRequestDTO) {
    return new Actor(actorRequestDTO.firstName(), actorRequestDTO.lastName(), actorRequestDTO.age());
  }

}
