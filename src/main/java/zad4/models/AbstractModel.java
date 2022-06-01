package zad4.models;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@MappedSuperclass
public class AbstractModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Date creationDate;
    private Date modificationDate;

    public AbstractModel() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractModel that = (AbstractModel) o;

        return Objects.equals(this.id,((AbstractModel) o).id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @PrePersist
    private void prePersistFunction() {
        if(this.creationDate == null) {
            this.creationDate = new Date(System.currentTimeMillis());
        }
        if(this.modificationDate == null) {
            this.modificationDate = new Date(System.currentTimeMillis());
        }
    }

    @PreUpdate
    public void preUpdateFunction(){
        this.modificationDate = new Date(System.currentTimeMillis());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }
}
