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
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

import java.time.Duration;
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
  private NumberField deleteMessageTime;

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

    H1 header = new H1("AskChat " + chatService.getName());
    header.getElement().getThemeList().add("dark");
    add(header);

    messageList = new MessageList();

    add(messageList, createMessageInputLayout(), createSettings());
    expand(messageList);

    messages.subscribe(empty -> {
      List<Message> currentMessages = chatService.getMessages();
      List<MessageListItem> messageListItems = currentMessages.stream()
          .map(currentMessage -> new MessageListItem(currentMessage.getContent(), currentMessage.getTimestamp(), currentMessage.getUser().getName()))
          .collect(Collectors.toList());
      messageList.setItems(messageListItems);
    });
  }

  private HorizontalLayout createMessageInputLayout() {
    HorizontalLayout messageInputLayout = new HorizontalLayout();
    messageInputLayout.setAlignItems(Alignment.CENTER);
    userSelection = new ComboBox<>();
    userSelection.setItemLabelGenerator((ItemLabelGenerator<User>) User::getName);
    userSelection.setPlaceholder("Benutzer auswählen");
    messageInputLayout.add(userSelection, createMessageInput());
    return messageInputLayout;
  }

  private MessageInput createMessageInput() {
    MessageInput messageInput = new MessageInput();
    messageInput.addSubmitListener(submitEvent -> {
      Double time = deleteMessageTime.getValue();
      chatService.addMessage(submitEvent.getValue(), Duration.ofSeconds(time != null && time > 0 ? time.longValue() : 60), userSelection.getValue());
      publisher.onNext(submitEvent.getValue());
    });
    return messageInput;
  }

  private VerticalLayout createSettings() {
    Label settings = new Label("Einstellungen");

    TextField userName = new TextField();
    userName.setPlaceholder("Benutzername");

    Button addUserButton = new Button("Benutzer hinzufügen");
    addUserButton.addClickShortcut(Key.ENTER);
    addUserButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    addUserButton.addClickListener(click -> {
      users.add(new User(userName.getValue(), ""));
      User currentUser = userSelection.getValue();
      userSelection.setItems(users);
      userSelection.setValue(currentUser != null ? currentUser : users.stream().findFirst().get());
      userName.clear();
    });

    HorizontalLayout createUserLayout = new HorizontalLayout();
    createUserLayout.add(userName, addUserButton);

    deleteMessageTime = new NumberField("Löschen nach");
    deleteMessageTime.setPlaceholder("Zeit in s");
    VerticalLayout layout = new VerticalLayout();
    layout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    layout.add(settings, createUserLayout, deleteMessageTime);
    return layout;
  }
}
