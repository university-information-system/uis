package at.ac.tuwien.inso.entity;

import javax.persistence.*;
import java.util.*;

@Entity
public class Lecturer extends UisUser {

    @ManyToMany(mappedBy = "lecturers")
    private List<Subject> subjects = new ArrayList<>();

    protected Lecturer() {

    }

    public Lecturer(String identificationNumber, String name, String email) {
        this(identificationNumber, name, email, null);
    }

    public Lecturer(String identificationNumber, String name, String email, UserAccount account) {
        super(identificationNumber, name, email, account);
    }

    public List<Subject> getSubjects() {
        return subjects;
    }
}
