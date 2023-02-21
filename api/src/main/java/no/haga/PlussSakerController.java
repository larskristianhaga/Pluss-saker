package no.haga;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/api/getPlusData")
public class PlussSakerController {

    @Get()
    public String index() {
        return "Example Response";
    }
}