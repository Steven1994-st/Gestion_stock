package com.gestion.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@MappedSuperclass
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "classe")
public class Persistent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Transient
    public String classe = getClass().getSimpleName();

    /**
     * Unique ID of this persistent object.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    /**
     * The creation date.
     * Date when this object is created.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", nullable = false , updatable = false)
    protected Date creationDate;

    /**
     * The last modification date.
     * Date when the last modification of this object occurred.
     */
    @Version
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modification_date", nullable = false)
    protected Date modificationDate;

    /**
     * Default constructor.
     */
    public Persistent() {
        Calendar c = Calendar.getInstance();
        creationDate = new Timestamp(c.getTimeInMillis());
        modificationDate = new Timestamp(c.getTimeInMillis());
    }

    /**
     * The object ID. This attribute is null if the object is not yet persisted.
     *
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     * the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * The date of creation
     *
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate
     *            the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setCreationDates(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * The date of last modification
     *
     * @return the modificationDate
     */
    public Date getModificationDate() {
        return modificationDate;
    }

    /**
     * @param modificationDate
     *            the modificationDate to set
     */
    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    @Override
    public String toString() {
        return "[" + getClass().getSimpleName() + " : " + getId() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((classe == null) ? 0 : classe.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Persistent other = (Persistent) obj;
        if (classe == null) {
            if (other.classe != null)
                return false;
        } else if (!classe.equals(other.classe))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
