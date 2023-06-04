package no.haga;


import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class PlussSaker {
    @Id
    @Index
    private String newsPaperName;
    private String newsPaperURL;
    private List<Checks> checks;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Checks {
        private Date checkedDateTime;
        private int numberOfPlusArticles;
        private int numberOfArticles;
    }
}
