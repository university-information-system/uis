package at.ac.tuwien.inso.entity;

import javax.persistence.*;
import java.util.*;

@Entity
public class Student extends UisUser {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyPlanRegistration> studyplans = new ArrayList<>();

    @ManyToMany
    private List<Course> courses = new ArrayList<>();

    protected Student() {
    }

    public Student(String name, String email) {
        this(name, email, null);
    }

    public Student(String name, String email, UserAccount account) {
        super(name, email, account);
    }
}
