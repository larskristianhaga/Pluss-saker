package no.haga;

import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Singleton
public class PlussSakerJob {

    private static final Logger LOG = LoggerFactory.getLogger(PlussSakerJob.class);

    @Inject
    Newspapers config;

    @Scheduled(cron = "${newspapers.cronSchedule}")
    public void executeEveryTen() throws IOException, InterruptedException {

        // Iterate over the list of newspaper to check for pluss articles.
        config.syncs.forEach(element -> {
            // Create a new HTTP request with the URL from the config.
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(element.url))
                    .GET()
                    .build();

            // Send the request and get the response.
            HttpResponse<String> response;
            try {
                response = HttpClient.newBuilder()
                        .build()
                        .send(request, HttpResponse.BodyHandlers.ofString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            var occurrencesPluss = countOccurrences(response.body(), element.plussSelector);
            var occurrencesArticles = countOccurrences(response.body(), element.articleSelector);

            LOG.info(String.valueOf(occurrencesPluss));
            LOG.info(String.valueOf(occurrencesArticles));
        });


    }

    // Gets the number of occurrences of the element in a given string.
    private int countOccurrences(String element, String searchTerm) {
        int count = 0;
        int index = 0;

        while ((index = element.indexOf(searchTerm, index)) != -1) {
            index++;
            count++;
        }
        return count;
    }
}