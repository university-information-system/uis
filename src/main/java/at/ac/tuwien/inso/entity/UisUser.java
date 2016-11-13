package at.ac.tuwien.inso.entity;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class UisUser {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserAccount account;

    protected UisUser() {

    }

    public UisUser(String name, String email, UserAccount account) {
        this.name = name;
        this.email = email;
        this.account = account;
    }

    public Long getId() {

        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Transient
    public boolean isActivated() {
        return account != null;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UisUser uisUser = (UisUser) o;

        if (getId() != null ? !getId().equals(uisUser.getId()) : uisUser.getId() != null) return false;
        if (name != null ? !name.equals(uisUser.name) : uisUser.name != null) return false;
        if (email != null ? !email.equals(uisUser.email) : uisUser.email != null) return false;
        return account != null ? account.equals(uisUser.account) : uisUser.account == null;

    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (account != null ? account.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UisUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", account=" + account +
                '}';
    }
}
