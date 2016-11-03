package at.ac.tuwien.inso.entity;

import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.*;

import javax.persistence.*;
import java.util.*;

import static java.util.Collections.*;

@Entity
public class UserAccount implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL)
    private Role role;

    public UserAccount() {
    }

    public UserAccount(String username, String password, Role role) {
        setUsername(username);
        setPassword(password);
        setRole(role);
    }

    public UserAccount setId(Long id) {
        this.id = id;
        return this;
    }

    public UserAccount setUsername(String username) {
        this.username = username;
        return this;
    }

    public UserAccount setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserAccount setRole(Role role) {
        this.role = role;
        role.setUserAccount(this);
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return singletonList(role);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
