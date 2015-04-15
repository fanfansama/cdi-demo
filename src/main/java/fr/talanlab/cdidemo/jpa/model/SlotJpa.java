package fr.talanlab.cdidemo.jpa.model;

import javax.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(name = "Slot.findAll", query = "select s from SlotJpa s")
})
public class SlotJpa {
    @Id
    private String id;

    @Version
    private long version;

    public SlotJpa() {
        // no-op
    }

    public SlotJpa(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(final long version) {
        this.version = version;
    }
}
