package at.ac.tuwien.inso.entity;

import javax.persistence.*;

@Entity
public class Lecturer extends UisUser {

    protected Lecturer() {

    }

    public Lecturer(String name, String email) {
        this(name, email, null);
    }

    public Lecturer(String name, String email, UserAccount account) {
        super(name, email, account);
    }
}
