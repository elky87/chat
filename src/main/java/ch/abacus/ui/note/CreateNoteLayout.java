package ch.abacus.ui.note;

import ch.abacus.MessageService;
import ch.abacus.data.Message;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Consumer;

public class CreateNoteLayout extends Div {

  private final MessageService messageService;

  private IntegerField minutesField;
  private IntegerField hoursField;
  private Checkbox deleteOnRead;
  private Button createButton;
  private TextArea messageField;
  private TextField passwordField;

  public CreateNoteLayout(MessageService messageService) {
    this.messageService = messageService;
    init();
  }

  private void init() {
    messageField = createMessageField();
    passwordField = createPasswordField();
    final Component durationField = createDurationField();
    createButton = getCreateButton();

    add(messageField, passwordField, durationField, createButton);
  }

  private TextField createPasswordField() {
    TextField messageField = new TextField("Passwort");
    return messageField;
  }

  private TextArea createMessageField() {
    TextArea messageField = new TextArea("Nachricht");
    messageField.setMinWidth(40, Unit.REM);
    messageField.setSizeFull();
    return messageField;
  }

  private Button getCreateButton() {
    Button createButton = new Button("Nachricht erstellen");
    createButton.addClickShortcut(Key.ENTER);
    createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    return createButton;
  }

  public void addCreateMessageListencer(Consumer<Message> consumer) {
    createButton.addClickListener(click -> {
      try {
        final Message message = messageService
            .generateMessage(messageField.getValue(), passwordField.getValue(), getDuration(), deleteOnRead.getValue());
        consumer.accept(message);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  private Duration getDuration() {
    return Duration.ofHours(Optional.ofNullable(hoursField.getValue()).orElse(0))
        .plusMinutes(Optional.ofNullable(minutesField.getValue()).orElse(0));
  }

  private Component createDurationField() {
    HorizontalLayout layout = new HorizontalLayout();
    layout.setAlignItems(FlexComponent.Alignment.END);
    minutesField = createTimeField("Minuten", 60, 0);
    hoursField = createTimeField("Stunden", 48, 24);
    deleteOnRead = createCheckbox("Nach Zugriff löschen", true);
    layout.add(minutesField, hoursField, deleteOnRead);
    return layout;
  }

  private Checkbox createCheckbox(String label, boolean defaultValue) {
    final Checkbox checkbox = new Checkbox(label);
    checkbox.setValue(defaultValue);
    return checkbox;
  }

  private IntegerField createTimeField(String label, int max, int defaultValue) {
    final IntegerField integerField = new IntegerField(label);
    integerField.setMax(max);
    integerField.setMin(0);
    integerField.setValue(defaultValue);
    return integerField;
  }

  public void clear(){
    messageField.setValue("");
    passwordField.setValue("");
  }

}
