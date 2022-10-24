package com.haga.plusssaker;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.util.Date;

@Entity
@Data
public class NewsPaperEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private final String newsPaperName;
    private final int numberOfPlusArticles;
    private final int numberOfArticles;
    private final Date syncedDate;
}