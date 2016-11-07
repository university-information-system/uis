package at.ac.tuwien.inso.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;

@Entity
public class Student {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private UserProfile userProfile;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyPlanRegistration> studyplans = new ArrayList<>();

    @ManyToMany
    private List<Course> courses = new ArrayList<>();

    protected Student() {

    }

    public Student(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public Long getId() {
        return id;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public List<StudyPlanRegistration> getStudyplans() {
        return unmodifiableList(studyplans);
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void registerTo(StudyPlanRegistration... studyplans) {
        this.studyplans.addAll(asList(studyplans));
    }

    public void unregisterFrom(StudyPlanRegistration... studyplans) {
        this.studyplans.removeAll(asList(studyplans));
    }

    public void addCourses(Course... courses) {
        this.courses.addAll(asList(courses));
    }

    public void removeCourses(Course... courses) {
        this.courses.removeAll(asList(courses));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (!id.equals(student.id)) return false;
        if (!userProfile.equals(student.userProfile)) return false;
        if (!studyplans.equals(student.studyplans)) return false;
        return courses.equals(student.courses);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + userProfile.hashCode();
        result = 31 * result + studyplans.hashCode();
        result = 31 * result + courses.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", userProfile=" + userProfile +
                ", studyplans=" + studyplans +
                ", courses=" + courses +
                '}';
    }
}
