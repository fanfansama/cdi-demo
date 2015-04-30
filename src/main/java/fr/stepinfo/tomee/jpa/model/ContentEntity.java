package fr.stepinfo.tomee.jpa.model;

import fr.stepinfo.tomee.jpa.model.enums.CategoryEnum;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@NamedQueries({
        @NamedQuery(name = "Content.findAll", query = "select c from ContentEntity c")
})
@Table(name = "CONTENT")
public class ContentEntity {
    @Id
    private String id;

    @Version
    private long version;

    private CategoryEnum category;

    public ContentEntity() {
    }

    public ContentEntity(final String id) {
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

    public CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }
}
