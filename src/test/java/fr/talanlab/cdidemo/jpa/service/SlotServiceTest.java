package fr.talanlab.cdidemo.jpa.service;


import fr.talanlab.cdidemo.DbCleaner;
import fr.talanlab.cdidemo.jpa.model.AttendeeJpa;
import fr.talanlab.cdidemo.jpa.model.SlotJpa;
import fr.talanlab.cdidemo.models.event.SlotUpdateEvent;
import fr.talanlab.cdidemo.producers.LoggerProducer;
import org.apache.openejb.junit.ApplicationComposer;
import org.apache.openejb.testing.Classes;
import org.apache.openejb.testing.ContainerProperties;
import org.apache.openejb.testing.Descriptor;
import org.apache.openejb.testing.Descriptors;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(ApplicationComposer.class)
@Classes(context = "app", cdi = true, value = {
        SlotService.class, LoggerProducer.class, DbCleaner.class
})
@Descriptors({
        @Descriptor(name = "persistence.xml", path = "src/main/webapp/WEB-INF/persistence.xml")
})
@ContainerProperties({
        @ContainerProperties.Property(name = "db", value = "new://Resource?type=DataSource"),
        @ContainerProperties.Property(name = "db.JdbcUrl", value = "jdbc:hsqldb:mem:test")
})
public class SlotServiceTest {
    @PersistenceContext(unitName = "talanLabUnit")
    private EntityManager em;

    @Inject
    private Event<SlotUpdateEvent> conferencesEvent;

    @Inject
    private UserTransaction ut;

    @Inject
    private SlotService slotService;

    // @{see securityService()}
    private String name;

    /*   @Test
       public void persistenceOnEvent() {
           final Slot conference = new Slot();
           conference.setSlotId("slot_" + new Date());
           conferencesEvent.fire(new SlotUpdateEvent(asList(conference)));

           final SlotJpa slot = em.find(SlotJpa.class, conference.getSlotId());
           assertNotNull(slot);
           assertEquals(0, slotService.count(slot));
       }
   */
    @Test
    public void increment() throws Exception {
        {
            final SlotJpa slot = new SlotJpa();
            slot.setId("incr");

            ut.begin();
            em.persist(slot);
            ut.commit();
        }
        assertEquals(0, slotService.count(em.find(SlotJpa.class, "incr")));

        for (int i = 0; i < 10; i++) {
            name = "i#" + i;
            slotService.addAttendeeToSlot("incr");
            assertEquals(i + 1, slotService.count(em.find(SlotJpa.class, "incr")));
        }
    }

    @Test
    public void decrement() throws Exception {
        {
            final SlotJpa slot = new SlotJpa();
            slot.setId("decr");

            ut.begin();
            em.persist(slot);
            for (int i = 0; i < 5; i++) {
                final AttendeeJpa attendee = new AttendeeJpa();
                attendee.setName("d#" + i);
                attendee.getSlot().add(slot);
                em.persist(attendee);
            }
            ut.commit();
        }
        assertEquals(5, slotService.count(em.find(SlotJpa.class, "decr")));

        for (int i = 0; i < 5; i++) {
            name = "d#" + i;
            slotService.removeAttendeeFromSlot("decr");
            assertEquals(5 - i - 1, slotService.count(em.find(SlotJpa.class, "decr")));
        }
        for (int i = 5; i < 10; i++) {
            name = "d#" + i;
            slotService.removeAttendeeFromSlot("decr");
            assertEquals(0, slotService.count(em.find(SlotJpa.class, "decr")));
        }
    }

    @Test
    public void getAttendeesBySlotId() throws Exception {
        SlotJpa slot1 = new SlotJpa();
        slot1.setId("slot1");
        SlotJpa slot2 = new SlotJpa();
        slot2.setId("slot2");
        ut.begin();
        em.persist(slot1);
        em.persist(slot2);
        ut.commit();
        //
        AttendeeJpa attendeeJpa1 = new AttendeeJpa();
        attendeeJpa1.getSlot().add(slot1);
        attendeeJpa1.getSlot().add(slot2);
        attendeeJpa1.setName("1");

        AttendeeJpa attendeeJpa2 = new AttendeeJpa();
        attendeeJpa2.getSlot().add(slot1);
        attendeeJpa2.setName("2");

        ut.begin();
        em.persist(attendeeJpa1);
        em.persist(attendeeJpa2);
        ut.commit();

        Map<String, Integer> attendeesBySlotId = slotService.getAttendeesCountGroupedBySlotId();

        assertNotNull(attendeesBySlotId);
        assertEquals(2, attendeesBySlotId.size());
        assertEquals(2, (int) attendeesBySlotId.get(slot1.getId()));
        assertEquals(1, (int) attendeesBySlotId.get(slot2.getId()));


    }

 /*   @Component
    public SecurityService securityService() {
        return new SecurityServiceImpl() {
            @Override
            public Principal getCallerPrincipal() {
                return new User(name);
            }
        };
    }

    */
}
