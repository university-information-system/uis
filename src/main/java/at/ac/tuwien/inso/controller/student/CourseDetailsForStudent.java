package at.ac.tuwien.inso.controller.student;

import at.ac.tuwien.inso.entity.*;

import java.math.*;
import java.util.*;

class CourseDetailsForStudent {

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
}
