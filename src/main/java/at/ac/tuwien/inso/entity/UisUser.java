package at.ac.tuwien.inso.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class UisUser {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(unique = true, nullable = false)
    private String identificationNumber;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserAccount account;

    protected UisUser() {

    }

    public UisUser(String identificationNumber, String name, String email, UserAccount account) {
        this.identificationNumber = identificationNumber;
        this.name = name;
        this.email = email;
        this.account = account;
    }

    public Long getId() {

        return id;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @JsonIgnore
    public UserAccount getAccount() {
        return account;
    }

    @Transient
    public boolean isActivated() {
        return account != null;
    }

    public final void activate(UserAccount account) {
        assert (this.account == null);

        this.account = account;
        adjustRole(account);
    }

    protected abstract void adjustRole(UserAccount account);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UisUser uisUser = (UisUser) o;

        if (getId() != null ? !getId().equals(uisUser.getId()) : uisUser.getId() != null) return false;
        if (getIdentificationNumber() != null ? !getIdentificationNumber().equals(uisUser.getIdentificationNumber()) : uisUser.getIdentificationNumber() != null)
            return false;
        if (getName() != null ? !getName().equals(uisUser.getName()) : uisUser.getName() != null) return false;
        if (getEmail() != null ? !getEmail().equals(uisUser.getEmail()) : uisUser.getEmail() != null) return false;
        return account != null ? account.equals(uisUser.account) : uisUser.account == null;

    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getIdentificationNumber() != null ? getIdentificationNumber().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        result = 31 * result + (account != null ? account.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UisUser{" +
                "id=" + id +
                ", identificationNumber='" + identificationNumber + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", account=" + account +
                '}';
    }
}
