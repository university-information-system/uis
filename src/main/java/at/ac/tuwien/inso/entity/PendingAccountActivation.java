package at.ac.tuwien.inso.entity;

import javax.persistence.*;
import java.util.*;

@Entity
public class PendingAccountActivation {

    @Id
    private String id;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private UisUser forUser;

    protected PendingAccountActivation() {

    }

    public PendingAccountActivation(UisUser forUser) {
        this(UUID.randomUUID().toString(), forUser);
    }

    public PendingAccountActivation(String id, UisUser forUser) {
        this.id = id;
        this.forUser = forUser;
    }

    public String getId() {
        return id;
    }

    public UisUser getForUser() {
        return forUser;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PendingAccountActivation that = (PendingAccountActivation) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return forUser != null ? forUser.equals(that.forUser) : that.forUser == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (forUser != null ? forUser.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PendingAccountActivation{" +
                "id='" + id + '\'' +
                ", forUser=" + forUser +
                '}';
    }
}
