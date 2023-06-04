package no.haga;

import com.googlecode.objectify.ObjectifyService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Component
public class PlussSakerJob {

    private static final Logger LOG = LoggerFactory.getLogger(PlussSakerJob.class);

    static {
        // Initialize Objectify, and the database connection to Google Cloud Datastore.
        ObjectifyService.init();
        ObjectifyService.register(PlussSaker.class);
    }

    @Autowired
    NewspapersConfig config;

    @Scheduled(cron = "${newspapers.cronSchedule}")
    public void executeCronJob() {

        LOG.info("Cron job started");

        // Iterate over the list of newspaper to check for pluss articles.
        config.getSyncs().forEach(element -> {
            // Create a new HTTP request with the URL from the config.
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(element.getUrl()))
                    .GET()
                    .build();

            // Send the request and get the response.
            HttpResponse<String> response;
            try {
                response = HttpClient.newBuilder()
                        .build()
                        .send(request, HttpResponse.BodyHandlers.ofString());
            } catch (Exception e) {
                LOG.error("Error while sending request to " + element.getUrl(), e);
                throw new RuntimeException(e);
            }

            // Count the number of occurrences of the plus and article selectors.
            var occurrencesPluss = countOccurrences(response.body(), element.getPlusSelector());
            LOG.info("Counted: " + occurrencesPluss + " number of plus articles, for newspaper: " + element.getName());
            var occurrencesArticles = countOccurrences(response.body(), element.getArticleSelector());
            LOG.info("Counted: " + occurrencesArticles + " number of total articles, for newspaper: " + element.getName());

            var check = new PlussSaker.Checks(new Date(), occurrencesPluss, occurrencesArticles);

            // Get the data from the database, based on the name of the newspaper.
            var savedData = getNewspaperDataFromDatastore(element.getName());

            // If savedData has a value, it's not the first sync, and we can add the new check to the list of checks.
            if (savedData != null && savedData.getChecks() != null) {
                // Add the new check to the list of checks.
                savedData.getChecks().add(check);
            } else { // If savedData is null, then create a new PlussSaker object.
                // Create a new PlussSaker object.
                savedData = new PlussSaker(element.getName(), element.getUrl(), List.of(check));
            }

            // Persist the data to the database.
            persistDataToDatastore(savedData);
        });
    }

    /**
     * Persists the data to the database.
     *
     * @param data The data to persist.
     */
    private void persistDataToDatastore(PlussSaker data) {
        ObjectifyService.run(() -> ofy().save().entity(data).now());
    }

    /**
     * Gets the data from the database, based on the name of the newspaper.
     *
     * @param newsPaperName The name of the newspaper.
     * @return The data from the database, based on the name of the newspaper.
     */
    private PlussSaker getNewspaperDataFromDatastore(String newsPaperName) {
        return ObjectifyService.run(() -> ofy().load().type(PlussSaker.class).id(newsPaperName).now());
    }

    /**
     * Gets the number of occurrences of the element in a given string.
     *
     * @param element    The string to search in.
     * @param searchTerm The string to search for.
     * @return The number of occurrences of the element in a given string.
     */
    public static int countOccurrences(String element, String searchTerm) {
        return StringUtils.countMatches(element, searchTerm);
    }
}