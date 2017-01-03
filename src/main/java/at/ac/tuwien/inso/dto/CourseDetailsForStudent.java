package at.ac.tuwien.inso.dto;

import at.ac.tuwien.inso.entity.*;

import java.math.*;
import java.util.*;
import java.util.stream.*;

public class CourseDetailsForStudent {

    private Long id;
    private String name;
    private String semester;
    private BigDecimal ects;
    private String description;
    private Boolean canEnroll;
    private List<String> tags;
    private List<Lecturer> lecturers;
    private List<SubjectForStudyPlan> studyplans;
    private List<Subject> preconditions;

    public CourseDetailsForStudent(Course course) {
        id = course.getId();
        name = course.getSubject().getName();
        semester = course.getSemester().getLabel();
        ects = course.getSubject().getEcts();
        description = course.getDescription();
        tags = course.getTags().stream().map(Tag::getName).collect(Collectors.toList());
        lecturers = course.getSubject().getLecturers();
        preconditions = course.getSubject().getRequiredSubjects();
    }

    public Long getId() {
        return id;
    }

    public CourseDetailsForStudent setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CourseDetailsForStudent setName(String name) {
        this.name = name;
        return this;
    }

    public String getSemester() {
        return semester;
    }

    public CourseDetailsForStudent setSemester(String semester) {
        this.semester = semester;
        return this;
    }

    public BigDecimal getEcts() {
        return ects;
    }

    public CourseDetailsForStudent setEcts(BigDecimal ects) {
        this.ects = ects;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CourseDetailsForStudent setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<String> getTags() {
        return tags;
    }

    public CourseDetailsForStudent setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public List<Lecturer> getLecturers() {
        return lecturers;
    }

    public CourseDetailsForStudent setLecturers(List<Lecturer> lecturers) {
        this.lecturers = lecturers;
        return this;
    }

    public List<SubjectForStudyPlan> getStudyplans() {
        return studyplans;
    }

    public CourseDetailsForStudent setStudyplans(List<SubjectForStudyPlan> studyplans) {
        this.studyplans = studyplans;
        return this;
    }

    public List<Subject> getPreconditions() {
        return preconditions;
    }

    public CourseDetailsForStudent setPreconditions(List<Subject> preconditions) {
        this.preconditions = preconditions;
        return this;
    }

    public Boolean getCanEnroll() {
        return canEnroll;
    }

    public CourseDetailsForStudent setCanEnroll(Boolean canEnroll) {
        this.canEnroll = canEnroll;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CourseDetailsForStudent that = (CourseDetailsForStudent) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (getSemester() != null ? !getSemester().equals(that.getSemester()) : that.getSemester() != null)
            return false;
        if (getEcts() != null ? !getEcts().equals(that.getEcts()) : that.getEcts() != null) return false;
        if (getDescription() != null ? !getDescription().equals(that.getDescription()) : that.getDescription() != null)
            return false;
        if (getCanEnroll() != null ? !getCanEnroll().equals(that.getCanEnroll()) : that.getCanEnroll() != null)
            return false;
        if (getTags() != null ? !getTags().equals(that.getTags()) : that.getTags() != null) return false;
        if (getLecturers() != null ? !getLecturers().equals(that.getLecturers()) : that.getLecturers() != null)
            return false;
        if (getStudyplans() != null ? !getStudyplans().equals(that.getStudyplans()) : that.getStudyplans() != null)
            return false;
        return getPreconditions() != null ? getPreconditions().equals(that.getPreconditions()) : that.getPreconditions() == null;

    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getSemester() != null ? getSemester().hashCode() : 0);
        result = 31 * result + (getEcts() != null ? getEcts().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getCanEnroll() != null ? getCanEnroll().hashCode() : 0);
        result = 31 * result + (getTags() != null ? getTags().hashCode() : 0);
        result = 31 * result + (getLecturers() != null ? getLecturers().hashCode() : 0);
        result = 31 * result + (getStudyplans() != null ? getStudyplans().hashCode() : 0);
        result = 31 * result + (getPreconditions() != null ? getPreconditions().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CourseDetailsForStudent{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", semester='" + semester + '\'' +
                ", ects=" + ects +
                ", description='" + description + '\'' +
                ", canEnroll=" + canEnroll +
                ", tags=" + tags +
                ", lecturers=" + lecturers +
                ", studyplans=" + studyplans +
                ", preconditions=" + preconditions +
                '}';
    }
}
