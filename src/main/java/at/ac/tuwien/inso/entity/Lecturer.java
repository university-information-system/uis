package at.ac.tuwien.inso.entity;

import org.jboss.aerogear.security.otp.api.Base32;

import javax.persistence.*;
import java.util.*;

@Entity
public class Lecturer extends UisUser {

    @ManyToMany(mappedBy = "lecturers")
    private List<Subject> subjects = new ArrayList<>();

    private String twoFactorSecret;

    protected Lecturer() {

    }

    public Lecturer(String identificationNumber, String name, String email) {
        this(identificationNumber, name, email, null);
    }

    public Lecturer(String identificationNumber, String name, String email, UserAccount account) {
        super(identificationNumber, name, email, account);
        this.twoFactorSecret = Base32.random();
    }

    @Override
    protected void adjustRole(UserAccount account) {
        account.setRole(Role.LECTURER);
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public String getTwoFactorSecret() {
        return twoFactorSecret;
    }

    public void setTwoFactorSecret(String twoFactorSecret) {
        this.twoFactorSecret = twoFactorSecret;
    }
}
