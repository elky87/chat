package ch.abacus;

import ch.abacus.data.Message;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

import java.util.UUID;

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
@PWA(name = "Vaadin Application",
        shortName = "Vaadin App",
        description = "This is an example Vaadin application.",
        enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@CssImport("./styles/styles.css")
public class MainView extends VerticalLayout {

    private String username;
    private final UnicastProcessor<Message> publisher;
    private final Flux<Message> messages;
    private MessageList messageList;

    /**
     * Construct a new Vaadin view.
     * <p>
     * Build the initial UI state for the user accessing the application.
     *
     * @param service The message service. Automatically injected Spring managed bean.
     */
    public MainView(@Autowired GreetService service,
                    UnicastProcessor<Message> publisher,
                    Flux<Message> messages) {
        this.publisher = publisher;
        this.messages = messages;
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        addClassName("main-view");

        H1 header = new H1("AbaChat");
        header.getElement().getThemeList().add("dark");
        add(header);

        askUsername();
    }

    private void askUsername() {
        HorizontalLayout layout = new HorizontalLayout();
        TextField usernameField = new TextField();
        usernameField.setHelperText("Benutzername");
        Button startButton = new Button("Chat starten");
        startButton.addClickShortcut(Key.ENTER);
        startButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        startButton.addClickListener(click -> {
            username = usernameField.getValue();
            remove(layout);
            showChat();
        });

        layout.add(usernameField, startButton);
        add(layout);
    }

    private void showChat() {
        messageList = new MessageList();
        add(messageList, createInputLayout());
        expand(messageList);
    }


    private Component createInputLayout() {
        HorizontalLayout layout = new HorizontalLayout();

        TextField messageField = new TextField();
        Button sendButton = new Button("Send");
        sendButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        sendButton.addClickListener(click -> {
            publisher.onNext(new Message(username, messageField.getValue()));
            messageField.clear();
            messageField.focus();

            messages.subscribe(message -> {
                messageList.add(
                    new Paragraph(message.getUserName() + ": " +
                                  message.getContent()));
            });
        });
        messageField.focus();

        layout.add(messageField, sendButton);

        layout.setWidth("100%");
        layout.expand(messageField);
        return layout;
    }

}
