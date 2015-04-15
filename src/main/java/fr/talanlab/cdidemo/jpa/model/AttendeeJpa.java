package fr.talanlab.cdidemo.jpa.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "Attendee.findByName",
                query = "select a from AttendeeJpa a where a.name = :name"),
        @NamedQuery(
                name = "Attendee.findByNameAndSlot",
                query = "select count(a) from AttendeeJpa a where a.name = :name and :slot member of a.slot"
        ),
        @NamedQuery(
                name = "Attendee.countBySlot",
                query = "select count(a) from AttendeeJpa a where :slot member of a.slot"
        ),
        @NamedQuery(
                name = "Attendee.countGroupByAllSlot",
                query = "select  s.id,count(a) from AttendeeJpa a inner join a.slot s group by s.id"
        )
})
public class AttendeeJpa {
    @Id
    @GeneratedValue // would be better to use a table generator but we have only 3h
    private long id;

    @Version
    private long version;

    @Column(unique = true) // not used as id cause in real life it will not be an id for a long time ;)
    private String name;

    @ManyToMany
    private Collection<SlotJpa> slot;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<SlotJpa> getSlot() {
        if (slot == null) {
            slot = new HashSet<>();
        }
        return slot;
    }

    @Entity
    @NamedQueries({
            @NamedQuery(name = "Slot.findAll", query = "select s from SlotJpa s")
    })
    public static class SlotJpa {
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
}
