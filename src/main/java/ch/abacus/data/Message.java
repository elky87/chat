package ch.abacus.data;

import lombok.Data;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Data
public class Message {

  private UUID id;
  private String content;
  private Duration selfDestruct;
  private Instant timestamp;
  private boolean selfDestructAfterRead;


}
