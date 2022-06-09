package ch.abacus;

import ch.abacus.data.ChatRoom;
import ch.abacus.view.ChatRoomView;
import ch.abacus.view.ChatView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.NavigationEvent;
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

        Button chatButton = new Button("Chat");
        chatButton.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> getUI().ifPresent(ui -> ui.navigate("chat")));;


        add(chatButton);
    }

    private H1 createHeader() {
        H1 header = new H1("Ask");
        header.getElement().getThemeList().add("dark");
        return header;
    }
}
