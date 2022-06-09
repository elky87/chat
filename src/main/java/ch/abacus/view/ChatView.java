package ch.abacus.view;

import ch.abacus.ChatService;
import ch.abacus.EncryptionService;
import ch.abacus.MessageService;
import ch.abacus.data.ChatRoom;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

@Route("chat")
public class ChatView extends VerticalLayout {



  private final ChatService chatService;
  private final EncryptionService encryptionService;
  private final MessageService messageService;
  private final UnicastProcessor<String> publisher;
  private final Flux<String> messages;

  public ChatView(@Autowired ChatService chatService,
                  @Autowired EncryptionService encryptionService,
                  @Autowired MessageService messageService,
                  UnicastProcessor<String> publisher,
                  Flux<String> messages) {
    this.chatService = chatService;
    this.encryptionService = encryptionService;
    this.messageService = messageService;
    this.publisher = publisher;
    this.messages = messages;

    addClassName("create-chat-view");

    H1 header = createHeader();
    HorizontalLayout chatRoomLayout = createGenerateChatRoomLayout();
    add(header, chatRoomLayout);
  }

  private HorizontalLayout createGenerateChatRoomLayout() {
    HorizontalLayout layout = new HorizontalLayout();

    TextField chatRoomName = new TextField();
    chatRoomName.setPlaceholder("Name");

    PasswordField chatRoomPassword = new PasswordField();
    chatRoomPassword.setPlaceholder("Passwort");

    Button createChatRoom = new Button("ChatRoom erstellen");
    createChatRoom.addClickShortcut(Key.ENTER);
    createChatRoom.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    createChatRoom.addClickListener(click -> {
      chatService.setPassword(chatRoomPassword.getValue());
      chatService.setName(chatRoomName.getValue());
      getUI().ifPresent(ui -> ui.navigate("chatroom"));
    });

    layout.add(chatRoomName, chatRoomPassword, createChatRoom);
    return layout;
  }

  private H1 createHeader() {
    H1 header = new H1("AskChat");
    header.getElement().getThemeList().add("dark");
    return header;
  }
}
