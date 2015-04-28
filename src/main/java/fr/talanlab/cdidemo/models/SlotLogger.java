package fr.talanlab.cdidemo.models;


import fr.talanlab.cdidemo.models.event.SlotUpdateEvent;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.logging.Logger;

public class SlotLogger {

    @Inject
    private Logger logger;

    public void logActivity(@Observes final SlotUpdateEvent conferences) {
        logger.info(String.format("Schedule activity (%d slots retrieved)", +conferences.getConferences().size()));
    }
}
