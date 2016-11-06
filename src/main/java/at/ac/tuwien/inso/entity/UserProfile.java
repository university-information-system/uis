package at.ac.tuwien.inso.entity;

import javax.persistence.*;

@Embeddable
public class UserProfile {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserAccount account;

    protected UserProfile() {
    }

    public UserProfile(String name, String email, UserAccount account) {
        this.name = name;
        this.email = email;
        this.account = account;
    }

    public String getName() {

        return name;
    }

    public String getEmail() {
        return email;
    }

    public UserAccount getAccount() {
        return account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserProfile that = (UserProfile) o;

        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (getEmail() != null ? !getEmail().equals(that.getEmail()) : that.getEmail() != null) return false;
        return account != null ? account.equals(that.account) : that.account == null;

    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        result = 31 * result + (account != null ? account.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", account=" + account +
                '}';
    }
}
