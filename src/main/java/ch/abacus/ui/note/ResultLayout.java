package ch.abacus.ui.note;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;

public class ResultLayout extends HorizontalLayout {

  private final Button newMessage;
  private final TextArea textArea;

  public ResultLayout() {
    textArea = new TextArea("Url");

    Button button = new Button("Kopieren (Zwischenablage) ", VaadinIcon.COPY.create());
    button.addClickListener(
        e -> UI.getCurrent().getPage().executeJs("navigator.clipboard.writeText($0.$server.doPaste(clipText)) ", textArea.getValue())
    );
    newMessage = new Button("Neue Nachricht erstellen");
    add(textArea, button, newMessage);
  }

  public void setMessage(String result) {
    textArea.setValue(result);
  }

  public void addNewMessageListener(Runnable runnable) {
    newMessage.addClickListener(e -> runnable.run());
  }

}
