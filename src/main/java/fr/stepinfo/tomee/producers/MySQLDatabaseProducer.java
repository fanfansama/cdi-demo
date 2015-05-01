package fr.stepinfo.tomee.producers;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public class MySQLDatabaseProducer {

    @Produces
    @PersistenceContext(unitName = "tomeeDemoUnit")
    private EntityManager em;

    public void dispose(@Disposes @Default EntityManager entityManager) {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }

}