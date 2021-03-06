package ch.abacus.data;

import lombok.Data;

import java.util.UUID;

@Data
public class User {

  private UUID id;
  private String name;
  private String password;
  private Integer userColor;

  public User(String name, String password) {
    this.name = name;
    this.password = password;
  }
}
