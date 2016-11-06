package at.ac.tuwien.inso.entity;

import javax.persistence.*;

@Entity
public class SubjectForStudyPlan {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private Subject subject;

    @Column(nullable = false)
    private Boolean mandatory;

    private Integer semesterRecommendation;

    protected SubjectForStudyPlan() {
    }

    public SubjectForStudyPlan(Subject subject, Boolean mandatory) {
        this(subject, mandatory, null);
    }

    public SubjectForStudyPlan(Subject subject, Boolean mandatory, Integer semesterRecommendation) {

        this.subject = subject;
        this.mandatory = mandatory;
        this.semesterRecommendation = semesterRecommendation;
    }

    public Long getId() {
        return id;
    }

    public Subject getSubject() {
        return subject;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public Integer getSemesterRecommendation() {
        return semesterRecommendation;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubjectForStudyPlan that = (SubjectForStudyPlan) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (subject != null ? !subject.equals(that.subject) : that.subject != null) return false;
        if (mandatory != null ? !mandatory.equals(that.mandatory) : that.mandatory != null) return false;
        return semesterRecommendation != null ? semesterRecommendation.equals(that.semesterRecommendation) : that.semesterRecommendation == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (mandatory != null ? mandatory.hashCode() : 0);
        result = 31 * result + (semesterRecommendation != null ? semesterRecommendation.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SubjectForStudyPlan{" +
                "id=" + id +
                ", subject=" + subject +
                ", mandatory=" + mandatory +
                ", semesterRecommendation=" + semesterRecommendation +
                '}';
    }
}
