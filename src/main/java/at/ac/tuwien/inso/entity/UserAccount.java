package at.ac.tuwien.inso.entity;

import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.*;

import javax.persistence.*;
import java.util.*;

import static java.util.Collections.*;

@Entity
public class UserAccount implements UserDetails {

    public static final PasswordEncoder PASSWORD_ENCODER = new StandardPasswordEncoder();

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @ManyToOne(optional = false)
    private Role role;

    protected UserAccount() {
    }

    public UserAccount(String username, String password, Role role) {
        this.username = username;
        this.password = PASSWORD_ENCODER.encode(password);
        this.role = role;
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
