package at.ac.tuwien.inso.entity;

import org.springframework.security.core.*;

import javax.persistence.*;

@Entity
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private User user;

    private String authority;

    public Role() {}

    public Role(String authority) {
        setAuthority(authority);
    }

    public Long getId() {
        return id;
    }

    public Role setId(Long id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Role setUser(User user) {
        this.user = user;
        return this;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public Role setAuthority(String authority) {
        this.authority = authority;
        return this;
    }
}
