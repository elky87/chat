package ch.abacus;

import ch.abacus.data.ChatRoom;
import ch.abacus.data.Message;
import ch.abacus.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private MessageService messageService;
    private MessageRepository messageRepository;
    private EncryptionService encryptionService;

    private final ChatRoom chatRoom = new ChatRoom();

    @Autowired
    public ChatService(MessageService messageService, MessageRepository messageRepository, EncryptionService encryptionService) {
        this.messageService = messageService;
        this.messageRepository = messageRepository;
        this.encryptionService = encryptionService;
    }

    public void setName(String name) {
        chatRoom.setName(name);
    }

    public void setPassword(String password) {
        chatRoom.setPassword(password);
    }

    public void addUser(User user) {
        chatRoom.getUsers().add(user);
    }

    public void addMessage(String message) {
        messageService.generateMessage(message, chatRoom.getPassword(), Duration.ofMinutes(1), false);
    }

    public void addMessage(String message, Duration duration) {
        messageService.generateMessage(message, chatRoom.getPassword(), duration, false);
    }

    public List<Message> getMessages() {
        return messageRepository.getAllMessages()
                .stream()
                .peek(encryptedMessage -> encryptedMessage.setContent(encryptionService.decryptWithoutException(encryptedMessage.getContent(), chatRoom.getPassword())))
                .sorted(Comparator.comparing(Message::getTimestamp))
                .collect(Collectors.toList());
    }
}
