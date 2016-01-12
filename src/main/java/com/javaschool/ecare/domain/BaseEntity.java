package com.javaschool.ecare.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Base entity with id and equals and hashcode methods
 */
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long newId) {
        id = newId;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;

        if ((obj.getClass() == this.getClass()) && (((BaseEntity)obj).getId() == this.getId())) {
            result = true;
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
