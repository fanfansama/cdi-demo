package fr.talanlab.cdidemo.jpa.service;


import fr.talanlab.cdidemo.jpa.model.AttendeeJpa;
import fr.talanlab.cdidemo.jpa.model.SlotJpa;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@ApplicationScoped
public class SlotService {
    @PersistenceContext(unitName = "talanLabUnit")
    private EntityManager em;

    @Inject
    @RequestScoped
    private Principal principal;

    @Transactional
    public SlotJpa addAttendeeToSlot(final String slotId) {
        final SlotJpa slot = findById(slotId);
        if (slot == null) {
            return null;
        }

        AttendeeJpa attendee = findAttendee(principal.getName());
        if (attendee == null) {
            attendee = createAttendee();
        }
        if (isAttendeeParticipatingToSlot(slot, attendee)) {
            return slot;
        }

        attendee.getSlot().add(slot);
        return slot;
    }

    @Transactional
    public SlotJpa removeAttendeeFromSlot(final String slotId) {
        final SlotJpa slot = findById(slotId);
        if (slot == null) {
            return null;
        }

        AttendeeJpa attendee = findAttendee(principal.getName());
        if (attendee == null || !isAttendeeParticipatingToSlot(slot, attendee)) {
            return slot;
        }

        attendee.getSlot().remove(slot);
        return slot;
    }

    @Transactional
    public SlotJpa findById(final String id) {
        return em.find(SlotJpa.class, id);
    }

    public int count(final SlotJpa slot) {
        return em.createNamedQuery("Attendee.countBySlot", Number.class)
                .setParameter("slot", slot)
                .getSingleResult()
                .intValue();
    }

    @Transactional
    public Collection<String> getAllocatedSlotIdsForAttendee(String name) {
        Collection<String> slotIds = new LinkedList<>();
        AttendeeJpa attendee = findAttendee(name);
        if (attendee != null) {
            attendee.getSlot().forEach(slotJpa -> slotIds.add(slotJpa.getId()));
        }
        return slotIds;
    }

    public Map<String, Integer> getAttendeesCountGroupedBySlotId() {
        return em.createNamedQuery("Attendee.countGroupByAllSlot", Object[].class)
                .getResultList().stream()
                .collect(toMap(tuple -> (String) tuple[0], tuple -> Number.class.cast(tuple[1]).intValue()));
    }

    private boolean isAttendeeParticipatingToSlot(final SlotJpa slot, final AttendeeJpa attendee) {
        return em.createNamedQuery("Attendee.findByNameAndSlot", Number.class)
                .setParameter("name", attendee.getName())
                .setParameter("slot", slot)
                .getSingleResult()
                .intValue() > 0;
    }

    private AttendeeJpa findAttendee(String name) {
        try {
            return em.createNamedQuery("Attendee.findByName", AttendeeJpa.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (final NoResultException nre) {
            return null;
        }
    }

    public List<AttendeeJpa> findAll() {
        try {
            return em.createNamedQuery("Attendee.findAll", AttendeeJpa.class)
                    .getResultList();
        } catch (final NoResultException nre) {
            return null;
        }
    }

    private AttendeeJpa createAttendee() {
        final AttendeeJpa attendee = new AttendeeJpa();
        attendee.setName(principal.getName());
        em.persist(attendee);
        return attendee;
    }

}
