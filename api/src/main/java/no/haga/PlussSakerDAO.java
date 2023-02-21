package no.haga;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class PlussSakerDAO {

    String NewsPaperName;
    String NewsPaperURL;
    List<Checks> Checks;

    @Data
    @AllArgsConstructor
    public static class Checks {
        Date CheckedDateTime;
        int NumberOfPlusArticles;
        int NumberOfArticles;
    }
}
