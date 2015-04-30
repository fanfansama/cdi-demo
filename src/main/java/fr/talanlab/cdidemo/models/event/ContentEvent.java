package fr.talanlab.cdidemo.models.event;


import fr.talanlab.cdidemo.jpa.model.ContentEntity;

import java.util.Collection;

public class ContentEvent {
    private final Collection<ContentEntity> contents;

    public ContentEvent(final Collection<ContentEntity> contents) {
        this.contents = contents;
    }

    public Collection<ContentEntity> getContents() {
        return contents;
    }
}
