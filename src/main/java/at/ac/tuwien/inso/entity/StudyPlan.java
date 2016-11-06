package at.ac.tuwien.inso.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;

@Entity
public class StudyPlan {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Embedded
    private EctsDistribution ectsDistribution;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<SubjectForStudyPlan> subjects = new ArrayList<>();

    protected StudyPlan() {
    }

    public StudyPlan(String name, EctsDistribution ectsDistribution) {
        this.name = name;
        this.ectsDistribution = ectsDistribution;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public EctsDistribution getEctsDistribution() {
        return ectsDistribution;
    }

    public List<SubjectForStudyPlan> getSubjects() {
        return unmodifiableList(subjects);
    }

    public void addSubjects(SubjectForStudyPlan... subjects) {
        this.subjects.addAll(asList(subjects));
    }

    public void removeSubjects(SubjectForStudyPlan... subjects) {
        this.subjects.removeAll(asList(subjects));
    }

    @Override
    public String toString() {
        return "StudyPlan{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ectsDistribution=" + ectsDistribution +
                ", subjects=" + subjects +
                '}';
    }
}
