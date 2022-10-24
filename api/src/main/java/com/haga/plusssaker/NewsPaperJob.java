package com.haga.plusssaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Date;

@Configuration
@EnableScheduling
public class NewsPaperJob {

    @Autowired
    WebClient webClient;

    @Value("${newspaper.vg.url}")
    String vgUrl;

    @Value("${newspaper.vg.pluss-selector}")
    String vgPlussSelector;

    @Value("${newspaper.vg.article-selector}")
    String vgArticleSelector;

    @Scheduled(fixedRate = 10000)
    public void getDataFromTheDifferentNewsPapers() {
        syncDataFromVG(vgUrl, vgPlussSelector, vgArticleSelector);
        // syncDataFromAO();
        // syncDataFromAmta();
    }


    private void syncDataFromVG(String vgUrl, String vgPlussSelector, String vgArticleSelector) {
        var response = getURLData(vgUrl);
        System.out.println(response);

        var numberOfOccurrences = countNumberOfOccurrences(response, vgPlussSelector);
        var numberOfArticles = countNumberOfOccurrences(response, vgArticleSelector);

        System.out.println(numberOfOccurrences);
        System.out.println(numberOfArticles);

        NewsPaperEntity entity = new NewsPaperEntity("VG", numberOfOccurrences, numberOfArticles, new Date());

        System.out.println(entity);

        // repository.save(entity);
    }

    private int countNumberOfOccurrences(String valueToCheck, String selector) {
        return valueToCheck.split(selector).length - 1;
    }

    String getURLData(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}

