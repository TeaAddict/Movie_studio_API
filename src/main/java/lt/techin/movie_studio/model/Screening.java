package lt.techin.movie_studio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "screenings")
public class Screening {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotNull
  @Size(min = 2, max = 100)
  private String theaterName;

  @NotNull
  private LocalDateTime screeningTime;

  public Screening() {
  }

  public long getId() {
    return id;
  }

  public String getTheaterName() {
    return theaterName;
  }

  public void setTheaterName(String theaterName) {
    this.theaterName = theaterName;
  }

  public LocalDateTime getScreeningTime() {
    return screeningTime;
  }

  public void setScreeningTime(LocalDateTime screeningTime) {
    this.screeningTime = screeningTime;
  }
}

//CREATE TABLE screenings (
//        id BIGINT PRIMARY KEY AUTO_INCREMENT,
//        theater_name VARCHAR(100) NOT NULL,
//        screening_time datetime NOT NULL,
//        movie_id BIGINT,
//        FOREIGN KEY (movie_id) REFERENCES movies(id)
//        );