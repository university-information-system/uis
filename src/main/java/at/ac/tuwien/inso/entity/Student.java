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

    public void registerTo(StudyPlanRegistration... studyplans) {
        this.studyplans.addAll(asList(studyplans));
    }

    public void unregisterFrom(StudyPlanRegistration... studyplans) {
        this.studyplans.removeAll(asList(studyplans));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (getId() != null ? !getId().equals(student.getId()) : student.getId() != null) return false;
        if (getUserProfile() != null ? !getUserProfile().equals(student.getUserProfile()) : student.getUserProfile() != null)
            return false;
        return getStudyplans() != null ? getStudyplans().equals(student.getStudyplans()) : student.getStudyplans() == null;

    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getUserProfile() != null ? getUserProfile().hashCode() : 0);
        result = 31 * result + (getStudyplans() != null ? getStudyplans().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", userProfile=" + userProfile +
                ", studyplans=" + studyplans +
                '}';
    }
}
