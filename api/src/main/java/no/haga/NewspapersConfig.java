package no.haga;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties("newspapers")
@Data
public class NewspapersConfig {

    private List<Elements> syncs;

    @Data
    public static class Elements {
        private String name;
        private String url;
        private String plusSelector;
        private String articleSelector;
    }
}