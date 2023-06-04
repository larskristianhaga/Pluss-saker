package no.haga;

import com.googlecode.objectify.ObjectifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

import static com.googlecode.objectify.ObjectifyService.ofy;

@RestController
@RequestMapping("/api/getPlusData")
public class PlussSakerController {

    static {
        // Initialize Objectify, and the database connection to Google Cloud Datastore.
        ObjectifyService.init();
        ObjectifyService.register(PlussSaker.class);
    }

    @Autowired
    NewspapersConfig config;

    @GetMapping()
    public ResponseEntity<ArrayList<PlussSaker>> getDataOfNewsPapers() {

        var retVal = new ArrayList<PlussSaker>();

        // Iterate over the list of newspaper to check for pluss articles.
        config.getSyncs().forEach(element -> retVal.add(ObjectifyService.run(() ->
                ofy().load().type(PlussSaker.class).id(element.getName()).now())));

        return ResponseEntity.ok(retVal);
    }
}