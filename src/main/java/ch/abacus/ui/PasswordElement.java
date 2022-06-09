package ch.abacus.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.apache.commons.lang3.RandomStringUtils;

public class PasswordElement extends HorizontalLayout {

  private final TextField passwordTextField;

  public PasswordElement() {
    setAlignItems(Alignment.END);
    setWidthFull();
    passwordTextField = new TextField("Passwort");
    passwordTextField.setWidthFull();
    final Button generate = new Button("", VaadinIcon.PLAY.create(), buttonClickEvent -> passwordTextField.setValue(generate()));
    final Button copy = new Button("",
                                   VaadinIcon.COPY.create(),
                                   e -> UI.getCurrent().getPage()
                                       .executeJs("navigator.clipboard.writeText($0) ", passwordTextField.getValue()));
    add(passwordTextField, generate, copy);
  }

  private String generate() {
    return RandomStringUtils.random(24, true, true);
  }

  public String getPassword() {
    return passwordTextField.getValue();
  }

  public void clear() {
     passwordTextField.setValue("");
  }

}
