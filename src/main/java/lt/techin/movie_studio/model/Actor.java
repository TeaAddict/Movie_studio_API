package lt.techin.movie_studio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "actors")
public class Actor {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotNull
  @Size(min = 2, max = 50)
  private String firstName;

  @NotNull
  @Size(min = 2, max = 50)
  private String lastName;

  @Min(18)
  @Max(100)
  private int age;

  public Actor(String firstName, String lastName, int age) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.age = age;
  }

  public Actor() {
  }

  public long getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }
}

//CREATE TABLE actors (
//        id BIGINT PRIMARY KEY AUTO_INCREMENT,
//        first_name VARCHAR(50) NOT NULL,
//        last_name VARCHAR(50) NOT NULL,
//        age INT NOT NULL
//);