package ch.abacus;

import ch.abacus.data.ChatRoom;
import ch.abacus.data.Message;
import ch.abacus.data.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {

  public ChatRoom create(String password) {
    return new ChatRoom();
  }

  public void addUser(User user) {

  }

  public void addMessage(Message message) {

  }

  public List<Message> getMessages() {
    return new ArrayList<>();
  }
}
