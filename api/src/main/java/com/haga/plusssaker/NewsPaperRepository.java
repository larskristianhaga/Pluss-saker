package com.haga.plusssaker;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsPaperRepository extends CrudRepository<NewsPaperEntity, Long> {

}