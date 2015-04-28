package fr.talanlab.cdidemo.rest;


import fr.talanlab.cdidemo.jpa.service.SlotService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Logger;

@ApplicationScoped
@Path("attendee")
public class AttendeeResource {

    @Inject
    private SlotService slotService;

    @Inject
    private Principal principal;

    @Inject
    private Logger LOG;

    @GET
    @Path("slots")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<String> getSlotIds() {

        LOG.info("find : " + principal.getName());
        return slotService.getAllocatedSlotIdsForAttendee(principal.getName());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<String> getAttendies() {
        LOG.info("getAttendies " + principal.getName());
        return Arrays.asList("plop", "plop");
    }
}
