package no.haga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PlussSakerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlussSakerApplication.class, args);
    }

}
