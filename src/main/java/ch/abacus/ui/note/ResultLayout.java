package ch.abacus.ui.note;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

public class ResultLayout extends VerticalLayout {

  private final Button newMessage;
  private final TextArea textArea;

  public ResultLayout() {
    setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    textArea = new TextArea("Url");
    textArea.setMinWidth(40, Unit.REM);

    final HorizontalLayout horizontalLayout = new HorizontalLayout();
    horizontalLayout.setAlignItems(Alignment.BASELINE);
    Button button = new Button("",
                               VaadinIcon.COPY.create(),
                               e -> UI.getCurrent().getPage()
                                   .executeJs("navigator.clipboard.writeText($0) ", textArea.getValue())
    );
    horizontalLayout.add(textArea, button);
    newMessage = new Button("Neue Nachricht erstellen");
    add(horizontalLayout, newMessage);
  }

  public void setMessage(String result) {
    textArea.setValue(result);
  }

  public void addNewMessageListener(Runnable runnable) {
    newMessage.addClickListener(e -> runnable.run());
  }

}
