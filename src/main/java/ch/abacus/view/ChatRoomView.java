package ch.abacus.view;

import ch.abacus.ChatService;
import ch.abacus.EncryptionService;
import ch.abacus.MessageService;
import ch.abacus.data.Message;
import ch.abacus.data.User;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Route("chatroom")
public class ChatRoomView extends VerticalLayout {

  private final ChatService chatService;
  private final MessageService messageService;
  private final EncryptionService encryptionService;
  private final UnicastProcessor<String> publisher;
  private final MessageList messageList;
  private final List<User> users = new ArrayList<>();
  private ComboBox<String> userSelection;

  public ChatRoomView(@Autowired ChatService chatService,
                      @Autowired MessageService messageService,
                      @Autowired EncryptionService encryptionService,
                      UnicastProcessor<String> publisher,
                      Flux<String> messages) {
    this.chatService = chatService;
    this.messageService = messageService;
    this.encryptionService = encryptionService;
    this.publisher = publisher;

    addClassName("chat-room-view");

    messageList = new MessageList();

    add(createHeader(), createAddUserLayout(), messageList, createMessageInputLayout());
    this.setAlignItems(Alignment.BASELINE);
    expand(messageList);

    messages.subscribe(empty -> {
      List<MessageListItem> messageListItems;

      List<Message> currentMessages = chatService.getMessages();
      messageListItems = currentMessages.stream()
          .map(currentMessage -> new MessageListItem(currentMessage.getContent(), currentMessage.getTimestamp(), currentMessage.getUser().getName()))
          .collect(Collectors.toList());
      messageList.setItems(messageListItems);
    });
  }

  private HorizontalLayout createMessageInputLayout() {
    HorizontalLayout messageInputLayout = new HorizontalLayout();
    userSelection = new ComboBox<>();
    userSelection.setPlaceholder("Benutzer auswählen");
    messageInputLayout.add(userSelection, createMessageInput());
    return messageInputLayout;
  }

  private MessageInput createMessageInput() {
    MessageInput messageInput = new MessageInput();
    messageInput.addSubmitListener(submitEvent -> {
      chatService.addMessage(submitEvent.getValue());
      publisher.onNext("Test");
    });
    return messageInput;
  }

  private HorizontalLayout createAddUserLayout() {
    HorizontalLayout layout = new HorizontalLayout();

    TextField userName = new TextField();
    userName.setPlaceholder("Benutzername");

    PasswordField userPassword = new PasswordField();
    userPassword.setPlaceholder("Passwort");

    Button addUserButton = new Button("Benutzer hinzufügen");
    addUserButton.addClickShortcut(Key.ENTER);
    addUserButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    addUserButton.addClickListener(click -> {
      users.add(new User(userName.getValue(), userPassword.getValue()));
      userSelection.setItems(userName.getValue());
    });

    layout.add(userName, userPassword, addUserButton);
    return layout;
  }

  private H1 createHeader() {
    H1 header = new H1("AskChat");
    header.getElement().getThemeList().add("dark");
    return header;
  }
}
