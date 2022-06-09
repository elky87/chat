package ch.abacus.data;

import lombok.Data;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Data
public class Message {

  private UUID id;
  private String userName;
  private String content;
  private Duration selfDestruct;
  private Instant timestamp;
  private boolean selfDestructAfterRead;

  public Message(String userName, String content) {
    this.userName = userName;
    this.content = content;
  }

  public String getUserName() {
    return userName;
  }

  public String getContent() {
    return content;
  }

}
