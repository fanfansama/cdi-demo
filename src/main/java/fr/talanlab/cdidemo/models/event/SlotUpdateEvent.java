package fr.talanlab.cdidemo.models.event;


import fr.talanlab.cdidemo.jpa.model.AttendeeJpa;

import java.util.Collection;

public class SlotUpdateEvent {
    private final Collection<AttendeeJpa> conferences;

    public SlotUpdateEvent(final Collection<AttendeeJpa> conferences) {
        this.conferences = conferences;
    }

    public Collection<AttendeeJpa> getConferences() {
        return conferences;
    }
}
