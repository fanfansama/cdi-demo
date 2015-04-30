package fr.stepinfo.tomee.models;

import fr.stepinfo.tomee.models.event.ContentEvent;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.logging.Logger;

public class ContentLogger {

    @Inject
    private Logger logger;

    public void logActivity(@Observes final ContentEvent conferences) {
        logger.info(String.format("Schedule activity (%d content retrieved)", +conferences.getContents().size()));
    }
}
