package at.ac.tuwien.inso.entity;

import org.springframework.security.core.*;

import javax.persistence.*;

@Entity
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String authority;

    protected Role() {
    }

    public Role(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        if (!id.equals(role.id)) return false;
        return authority.equals(role.authority);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + authority.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", authority='" + authority + '\'' +
                '}';
    }
}
