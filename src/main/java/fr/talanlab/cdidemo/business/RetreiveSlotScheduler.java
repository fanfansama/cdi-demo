package fr.talanlab.cdidemo.business;


import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;
import java.util.logging.Logger;

import static javax.ejb.LockType.READ;
import static javax.ejb.TransactionAttributeType.SUPPORTS;

@Singleton
@Startup
@Lock(READ)
@TransactionAttribute(SUPPORTS)
public class RetreiveSlotScheduler {

    @Inject
    private Logger logger;

    @PostConstruct
    private void init() {
        try {
            updateConferences();
        } catch (final Exception ignore) {
            // no-op: let's timer try later
            logger.throwing(this.getClass().getName(), "@PostConstruct::init()", ignore);
        }
    }

    @Lock(READ)
    @Schedule(hour = "*", minute = "*")
    public void updateConferences() {
        logger.fine("=== Schedule ===");

    }
}
