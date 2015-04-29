package fr.talanlab.cdidemo.models.event;


import fr.talanlab.cdidemo.jpa.model.SlotJpa;

import java.util.Collection;

public class SlotUpdateEvent {
    private final Collection<SlotJpa> conferences;

    public SlotUpdateEvent(final Collection<SlotJpa> conferences) {
        this.conferences = conferences;
    }

    public Collection<SlotJpa> getConferences() {
        return conferences;
    }
}
