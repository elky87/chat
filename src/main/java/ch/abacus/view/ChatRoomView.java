package ch.abacus.view;

import ch.abacus.ChatService;
import ch.abacus.EncryptionService;
import ch.abacus.MessageService;
import ch.abacus.data.Message;
import ch.abacus.data.User;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
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
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@CssImport("./styles/styles.css")
@Push
public class ChatRoomView extends VerticalLayout {

  private final ChatService chatService;
  private final MessageService messageService;
  private final EncryptionService encryptionService;
  private final UnicastProcessor<String> publisher;
  private final MessageList messageList;
  private final List<User> users = new ArrayList<>();
  private ComboBox<User> userSelection;

  public ChatRoomView(@Autowired ChatService chatService,
                      @Autowired MessageService messageService,
                      @Autowired EncryptionService encryptionService,
                      UnicastProcessor<String> publisher,
                      Flux<String> messages) {
    this.chatService = chatService;
    this.messageService = messageService;
    this.encryptionService = encryptionService;
    this.publisher = publisher;

    setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    addClassName("main-view");

    H1 header = new H1("AskChat");
    header.getElement().getThemeList().add("dark");
    add(header);

    messageList = new MessageList();

    add(createAddUserLayout(), messageList, createMessageInputLayout());
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
    userSelection.setItemLabelGenerator((ItemLabelGenerator<User>) User::getName);
    userSelection.setPlaceholder("Benutzer auswählen");
    messageInputLayout.add(userSelection, createMessageInput());
    return messageInputLayout;
  }

  private MessageInput createMessageInput() {
    MessageInput messageInput = new MessageInput();
    messageInput.addSubmitListener(submitEvent -> {
      chatService.addMessage(submitEvent.getValue(), userSelection.getValue());
      publisher.onNext(submitEvent.getValue());
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
      userSelection.setItems(users);
    });

    layout.add(userName, userPassword, addUserButton);
    return layout;
  }
}
