package ch.abacus.data;

import lombok.Data;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Data
public class Message {

  private UUID id;
  private User user;
  private String content;
  private Duration selfDestruct;
  private Instant timestamp;
  private boolean selfDestructAfterRead;

  public Message() {
  }

  public Message(User user, String content) {
    this.user = user;
    this.content = content;
  }

  public boolean isDurationOver() {
    Duration durationBetweenNowAndCreation = Duration.between(timestamp, Instant.now());
    return selfDestruct.compareTo(durationBetweenNowAndCreation) < 0;
  }
}
