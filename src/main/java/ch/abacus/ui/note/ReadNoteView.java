package ch.abacus.ui.note;

import ch.abacus.EncryptionService;
import ch.abacus.MessageService;
import ch.abacus.data.Message;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;
import java.util.UUID;

@Route("read-note")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@CssImport("./styles/styles.css")
public class ReadNoteView extends VerticalLayout implements HasUrlParameter<String> {

  private final MessageService messageService;
  private final EncryptionService encryptionService;
  private final HorizontalLayout passwortLayout;
  private final TextArea messageLabel;

  private UUID currentUuid;

  public ReadNoteView(MessageService messageService,
                      EncryptionService encryptionService) {
    this.messageService = messageService;
    this.encryptionService = encryptionService;

    setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
    addClassName("main-view");

    H1 header = new H1("AbaNote");
    header.getElement().getThemeList().add("dark");
    add(header);

    passwortLayout = new HorizontalLayout();
    TextField passwort = new TextField("Passwort");
    passwortLayout.add(passwort);

    final Button button = new Button("Bestätigen");

    passwortLayout.add(button);
    passwortLayout.setVisible(false);
    add(passwortLayout);

    messageLabel = new TextArea("Nachricht");
    messageLabel.setVisible(false);
    add(messageLabel);

    button.addClickListener(event -> {

      messageService.getMessage(currentUuid).ifPresent(message -> {
        try {
          final String decrypt = encryptionService.decrypt(message.getContent(), Optional.ofNullable(passwort.getValue()).orElse(""));
          messageLabel.setValue(decrypt);
          messageLabel.setVisible(true);
          passwortLayout.setVisible(false);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                 InvalidKeyException | BadPaddingException | IllegalBlockSizeException | InvalidKeySpecException e) {
          Notification.show("Falsches Passwort");
        }
      });

    });
  }

  @Override
  public void setParameter(BeforeEvent beforeEvent, String uuid) {
    if (uuid != null) {
      try {
        currentUuid = UUID.fromString(uuid);
        final Optional<Message> message = messageService.getMessage(currentUuid);
        if (message
            .isPresent()) {
          String decrypt = encryptionService.decrypt(message.get().getContent(), "");
          messageLabel.setValue(decrypt);
          messageLabel.setVisible(true);
        } else {
          Notification.show("Nachricht existiert nicht");
        }
      } catch (Exception e) {
        passwortLayout.setVisible(true);
      }
    }
  }
}
