package fr.talanlab.cdidemo;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Startup
@Singleton
public class DbCleaner {
    @PersistenceContext(unitName = "talanLabUnit")
    private EntityManager em;

    @Inject
    private Logger logger;

    @PreDestroy
    private void cleanSlots() {
        logger.log(Level.INFO, "Cleaned up Slot     ({0} entries)", em.createQuery("delete from SlotJpa").executeUpdate());
        logger.log(Level.INFO, "Cleaned up Attendee ({0} entries)", em.createQuery("delete from AttendeeJpa").executeUpdate());
    }
}
