package at.ac.tuwien.inso.entity;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Student extends UisUser {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyPlanRegistration> studyplans = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Course> dismissedCourses = new ArrayList<>();

    protected Student() {
    }

    public Student(String identificationNumber, String name, String email) {
        this(identificationNumber, name, email, null);
    }

    public Student(String identificationNumber, String name, String email, UserAccount account) {
        super(identificationNumber, name, email, account);
    }

    @Override
    protected void adjustRole(UserAccount account) {
        account.setRole(Role.STUDENT);
    }

    public List<StudyPlanRegistration> getStudyplans() {
        return studyplans;
    }

    public Student addStudyplans(StudyPlanRegistration... studyplans) {
        this.studyplans.addAll(asList(studyplans));
        return this;
    }

    public List<Course> getDismissedCourses() {
        return dismissedCourses;
    }

    public void setDismissedCourses(List<Course> dismissedCourses) {
        this.dismissedCourses = dismissedCourses;
    }

    public Student addDismissedCourse(Course... dismissedCourse) {
        this.dismissedCourses.addAll(asList(dismissedCourse));
        return this;
    }
}
