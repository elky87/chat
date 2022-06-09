package ch.abacus.data;

import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
public class ChatRoom {

  private UUID id;
  private String name;
  private Set<User> users;
  private List<Message> messages;

}
