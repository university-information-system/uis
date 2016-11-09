package at.ac.tuwien.inso.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Lecturer extends UisUser {

    @ManyToMany(mappedBy = "lecturers")
    private List<Subject> subjects = new ArrayList<>();

    protected Lecturer() {

    }

    public Lecturer(String name, String email) {
        this(name, email, null);
    }

    public Lecturer(String name, String email, UserAccount account) {
        super(name, email, account);
    }

    public List<Subject> getSubjects() {
        return subjects;
    }
}
