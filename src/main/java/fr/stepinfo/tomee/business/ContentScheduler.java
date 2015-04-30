package fr.stepinfo.tomee.business;


import fr.stepinfo.tomee.jpa.service.ContentService;
import fr.stepinfo.tomee.models.event.ContentEvent;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.logging.Logger;

import static javax.ejb.LockType.READ;
import static javax.ejb.TransactionAttributeType.SUPPORTS;

@Singleton
@Startup
@Lock(READ)
@TransactionAttribute(SUPPORTS)
public class ContentScheduler {

    @Inject
    private Logger LOG;

    @Inject
    private Event<ContentEvent> contentEvent;

    @Inject
    private ContentService contentService;

    @PostConstruct
    private void init() {
        try {
            listContent();
        } catch (final Exception ignore) {
            // no-op: let's timer try later
            LOG.throwing(this.getClass().getName(), "@PostConstruct::init()", ignore);
        }
    }

    @Lock(READ)
    @Schedule(hour = "*", minute = "*")
    public void listContent() {
        LOG.fine("=== Schedule ===");
        contentEvent.fire(new ContentEvent(contentService.findAll()));
    }
}
