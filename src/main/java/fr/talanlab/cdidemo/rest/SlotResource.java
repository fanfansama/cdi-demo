package fr.talanlab.cdidemo.rest;


import fr.talanlab.cdidemo.jpa.service.SlotService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@ApplicationScoped
@Path("conferences")
public class SlotResource {

//	@Inject
//  private SlotsHolder conferences;

    @Inject
    private SlotService slotService;

    @GET
    @Path("attendees")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Integer> getAttendeesBySlotId() {
        return slotService.getAttendeesCountGroupedBySlotId();
    }

    /*
    @Inject
    private MessageBroadCaster messageBroadCaster;

	@GET
	@Benchmark
    @Produces(MediaType.APPLICATION_JSON)
	public Collection<Slot> getConferences(){
		return conferences.getConferences();
	}



    @HEAD
    @Path("attendees/increment/{id}")
    public void iGoToConference(@PathParam("id") final String slotId) {
        broadcast(slotService.addAttendeeToSlot(slotId));
    }

    @HEAD
    @Path("attendees/decrement/{id}")
    public void iDontGoAnymoreToConference(@PathParam("id") final String slotId) {
        broadcast(slotService.removeAttendeeFromSlot(slotId));
    }

    private void broadcast(final SlotJpa slot) {
        final SlotAttendees up = new SlotAttendees();
        up.setAttendees(slotService.count(slot));
        up.setSlotId(slot.getId());
        messageBroadCaster.broadcast(up);
    }  */
}
