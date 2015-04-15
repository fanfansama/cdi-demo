package fr.talanlab.cdidemo.rest;


import fr.talanlab.cdidemo.jpa.service.SlotService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.security.Principal;
import java.util.Collection;

@ApplicationScoped
@Path("attendee")
public class AttendeeResource {

    @Inject
    private SlotService slotService;

    @Inject
    private Principal principal;

    @GET
    @Path("slots")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<String> getSlotIds() {
        return slotService.getAllocatedSlotIdsForAttendee(principal.getName());
    }
}
