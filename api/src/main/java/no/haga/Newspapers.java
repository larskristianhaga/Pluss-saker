package no.haga;

import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.Data;

import java.util.List;

@ConfigurationProperties("newspapers")
public class Newspapers {

    List<Elements> syncs;

    @Data
    static class Elements {
        String name;
        String url;
        String plussSelector;
        String articleSelector;
    }
}