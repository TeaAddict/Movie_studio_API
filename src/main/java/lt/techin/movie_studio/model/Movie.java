package lt.techin.movie_studio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "movies")
public class Movie {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotNull
  @Size(min = 2, max = 100)
  private String name;

  @NotNull
  @Size(min = 2, max = 50)
  private String director;

  @Size(max = 1000)
  private String description;

  //  @NotNull(message = "idk test")
  //  @NotEmpty(message = "My guy pls put some sunscreening")
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "movie_id")
  private List<Screening> screenings;

  @ManyToMany
  @JoinTable(
          name = "movies_actors",
          joinColumns = @JoinColumn(name = "movie_id"),
          inverseJoinColumns = @JoinColumn(name = "actor_id")
  )
  private List<Actor> actors;

  public Movie(String name, String director, String description) {
    this.name = name;
    this.director = director;
    this.description = description;
  }

  public Movie(String name, String director) {
    this(name, director, null);
  }

  public Movie() {
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDirector() {
    return director;
  }

  public void setDirector(String director) {
    this.director = director;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<Screening> getScreenings() {
    return screenings;
  }

  public void setScreenings(List<Screening> screenings) {
    this.screenings = screenings;
  }

  public List<Actor> getActors() {
    return actors;
  }

  public void setActors(List<Actor> actors) {
    this.actors = actors;
  }
}

//CREATE TABLE movies (
//        id BIGINT PRIMARY KEY AUTO_INCREMENT,
//        name VARCHAR(100) NOT NULL,
//        director VARCHAR(50) NOT NULL,
//        description VARCHAR(1000)
//);

//CREATE TABLE movies_actors (
//        movie_id BIGINT NOT NULL,
//        actor_id BIGINT NOT NULL,
//        PRIMARY KEY (movie_id, actor_id),
//        FOREIGN KEY (movie_id) REFERENCES movies(id),
//        FOREIGN KEY (actor_id) REFERENCES actors(id)
//);