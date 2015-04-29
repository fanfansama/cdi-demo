package fr.talanlab.cdidemo.rest;


import fr.talanlab.cdidemo.jpa.model.SlotJpa;
import fr.talanlab.cdidemo.jpa.service.SlotService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@ApplicationScoped
@Path("conferences")
public class SlotResource {

    @Inject
    private SlotService slotService;

    @GET
    @Path("attendees")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Integer> getAttendeesBySlotId() {
        return slotService.getAttendeesCountGroupedBySlotId();
    }

    @GET
    @Path("attendees/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SlotJpa iGoToConference(@PathParam("id") final String slotId) {
        return slotService.addAttendeeToSlot(slotId);
    }

    @DELETE
    @Path("attendees/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SlotJpa iDontGoAnymoreToConference(@PathParam("id") final String slotId) {
        return slotService.removeAttendeeFromSlot(slotId);
    }

}
