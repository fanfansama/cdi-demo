package fr.talanlab.cdidemo.jpa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Collection;
import java.util.HashSet;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "User.findAll",
                query = "select a from UserEntity a"),
        @NamedQuery(
                name = "User.findByName",
                query = "select a from UserEntity a where a.name = :name"),
        @NamedQuery(
                name = "User.findByNameAndContent",
                query = "select count(a) from UserEntity a where a.name = :name and :content member of a.contents"
        ),
        @NamedQuery(
                name = "User.countByContent",
                query = "select count(a) from UserEntity a where :content member of a.contents"
        ),
        @NamedQuery(
                name = "User.countGroupByAllContent",
                query = "select  s.id,count(a) from UserEntity a inner join a.contents s group by s.id"
        )
})
@Table(name = "USER")
public class UserEntity {
    @Id
    @GeneratedValue // would be better to use a table generator but we have only 3h
    private long id;

    @Version
    private long version;

    @Column(unique = true) // not used as id cause in real life it will not be an id for a long time ;)
    private String name;

    @ManyToMany
    private Collection<ContentEntity> contents;

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

    public Collection<ContentEntity> getContents() {
        if (contents == null) {
            contents = new HashSet<>();
        }
        return contents;
    }

}
