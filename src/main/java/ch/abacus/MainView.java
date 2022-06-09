package ch.abacus;

import ch.abacus.ui.note.CreateNoteView;
import ch.abacus.view.ChatView;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and
 * use @Route annotation to announce it in a URL as a Spring managed
 * bean.
 * Use the @PWA annotation make the application installable on phones,
 * tablets and some desktop browsers.
 * <p>
 * A new instance of this class is created for every new user and every
 * browser tab/window.
 */
@Route
@PWA(name = "AbaChat Application",
        shortName = "AbaChat App",
        description = "stat page to choose note or chat",
        enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@CssImport("./styles/styles.css")
@Push
public class MainView extends VerticalLayout {

    public MainView(@Autowired ChatService chatService,
                    @Autowired EncryptionService encryptionService,
                    @Autowired MessageService messageService,
                    UnicastProcessor<String> publisher,
                    Flux<String> messages) {

        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        addClassName("main-view");

        H1 header = createHeader();
        add(header);

        HorizontalLayout layout = new HorizontalLayout();
        layout.setAlignItems(Alignment.CENTER);
        layout.setSpacing(true);
        layout.setWidthFull();
        layout.setHeight("500px");

        //Button noteButton = new Button("Ask - Note");
        final Button noteButton = new Button(new Image("image/sticky-note-line.png", "stickyNote"));

        noteButton.setWidth(50, Unit.PERCENTAGE);
        noteButton.setHeight(50, Unit.PERCENTAGE);

        noteButton.addClickListener(e ->
                                        noteButton.getUI().ifPresent(ui -> ui.navigate(CreateNoteView.class)));

        //Button chatButton = new Button("Ask - Chat");
        Button chatButton = new Button(new Image("image/chat-private-fill.png", "privateChat"));
        chatButton.setWidth(50, Unit.PERCENTAGE);
        chatButton.setHeight(50, Unit.PERCENTAGE);
        chatButton.addClickListener(e ->
                                        chatButton.getUI().ifPresent(ui -> ui.navigate(ChatView.class)));

        layout.add(noteButton, chatButton);

        add(layout);
    }

    private H1 createHeader() {
        H1 header = new H1("Ask - Abacus sichere Kommunikation");
        header.getElement().getThemeList().add("dark");
        return header;
    }
}
