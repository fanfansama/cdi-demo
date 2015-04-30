package fr.stepinfo.tomee;

import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Level;
import java.util.logging.Logger;

@Startup
@Singleton
public class DbCleaner {
    @PersistenceContext(unitName = "talanLabUnit")
    private EntityManager em;

    @Inject
    private Logger logger;

    @PreDestroy
    private void cleanSlots() {
        logger.log(Level.INFO, "Cleaned up Slot     ({0} entries)", em.createQuery("delete from ContentEntity").executeUpdate());
        logger.log(Level.INFO, "Cleaned up Attendee ({0} entries)", em.createQuery("delete from UserEntity").executeUpdate());
    }
}
