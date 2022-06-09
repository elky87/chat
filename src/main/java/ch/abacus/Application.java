package ch.abacus;

import ch.abacus.data.Message;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    @Bean
    UnicastProcessor<Message> publisher(){
        return UnicastProcessor.create();
    }
    @Bean
    Flux<Message> messages(UnicastProcessor<Message> publisher) {
        return publisher.replay(30).autoConnect();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }

}
