package fr.stepinfo.tomee.producers;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public class MySQLDatabaseProducer {

    @Produces
    @PersistenceContext(unitName = "talanLabUnit")
    private EntityManager em;


}