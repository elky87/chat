package ch.abacus.ui.note;

import ch.abacus.MessageService;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("create-note")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@CssImport("./styles/styles.css")
public class CreateNoteView extends VerticalLayout {

  @Autowired
  public CreateNoteView(MessageService messageService) {
    setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    addClassName("main-view");

    H1 header = new H1("AbaNote");
    header.getElement().getThemeList().add("dark");
    add(header);

    final ResultLayout resultLayout = new ResultLayout();
    final CreateNoteLayout createNoteLayout = new CreateNoteLayout(messageService);
    resultLayout.addNewMessageListener(() -> {
      remove(resultLayout);
      add(createNoteLayout);
    });
    createNoteLayout.addCreateMessageListencer(message -> {
      remove(createNoteLayout);
      resultLayout.setMessage("http://localhost:8080/read-note/" + message.getId());
      add(resultLayout);
    });
    add(createNoteLayout);
  }

}
