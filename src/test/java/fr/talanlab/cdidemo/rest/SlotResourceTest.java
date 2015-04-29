package fr.talanlab.cdidemo.rest;


import fr.talanlab.cdidemo.jpa.model.SlotJpa;
import fr.talanlab.cdidemo.jpa.service.SlotService;
import fr.talanlab.cdidemo.mock.BaseTestConfig;
import org.junit.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;
import static org.junit.Assert.assertEquals;

public class SlotResourceTest extends BaseTestConfig {


    /*@Test
    public void getConferences() {
        assertConferences(
                ClientBuilder.newBuilder().build()
                        .register(new JohnzonProvider())
                        .target(container.getInstance(Container.class).getRoot().toExternalForm() + "app/rest/conferences")
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .get(new GenericType<List<Slot>>() {
                        }));
    } */

    @Test
    public void incrDecr() {
        final String slotId = "conf_room5_friday_14_9h30_10h30";
        final SlotService slots = app.getInstance(JavaEEEasy.class).getSlotService();

        final String authorization = "Basic " + printBase64Binary("jonathan:secret".getBytes());
        final WebTarget client = ClientBuilder.newBuilder().build()
                .target(container.getInstance(Container.class).getRoot().toExternalForm() + "app/rest/conferences/attendees");

        {
            slots.save(new SlotJpa(slotId));
            final SlotJpa slot = slots.findById(slotId);
            assertEquals(0, slots.count(slot));
        }
        {
            Response response = client.path("{slot}").resolveTemplate("slot", "conf_room5_friday_14_9h30_10h30")
                    .request()
                    .header("Authorization", authorization)
                    .get();
            assertEquals(response.getStatus(), 200);
            assertEquals(1, slots.count(slots.findById(slotId)));
        }
        {
            Response response = client.path("{slot}").resolveTemplate("slot", "conf_room5_friday_14_9h30_10h30")
                    .request()
                    .header("Authorization", authorization)
                    .delete();
            assertEquals(response.getStatus(), 200);
            assertEquals(0, slots.count(slots.findById(slotId)));
        }
    }
}