package no.haga;

import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Date;

@Singleton
public class PlussSakerJob {

    private static final Logger LOG = LoggerFactory.getLogger(PlussSakerJob.class);

    @Inject
    NewspapersConfig config;

    @Scheduled(cron = "${newspapers.cronSchedule}")
    public void executeEveryTen() {

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

            var check = new PlussSakerDAO.Checks(new Date(), occurrencesPluss, occurrencesArticles);
            var checksList = new ArrayList<PlussSakerDAO.Checks>();
            checksList.add(check);

            var retVal = new PlussSakerDAO(element.getName(), element.getUrl(), checksList);

            LOG.info(String.valueOf(occurrencesPluss));
            LOG.info(String.valueOf(occurrencesArticles));
        });
    }

    // Gets the number of occurrences of the element in a given string.
    private int countOccurrences(String element, String searchTerm) {
        return StringUtils.countMatches(element, searchTerm);
    }
}