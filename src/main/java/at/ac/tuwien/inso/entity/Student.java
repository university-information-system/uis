package at.ac.tuwien.inso.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

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

    public List<StudyPlanRegistration> getStudyplans() {
        return studyplans;
    }

    public Student addStudyplans(StudyPlanRegistration... studyplans) {
        this.studyplans.addAll(asList(studyplans));
        return this;
    }
}
