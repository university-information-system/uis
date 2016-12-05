package at.ac.tuwien.inso.entity;

/**
 * Note: this is not an entity, just a wrapper.
 */
public class SubjectWithGrade {

    private SubjectForStudyPlan subjectForStudyPlan;
    private Grade grade;
    private SubjectType subjectType;

    public SubjectWithGrade(SubjectForStudyPlan subjectForStudyPlan, Grade grade, SubjectType subjectType) {
        this.subjectForStudyPlan = subjectForStudyPlan;
        this.grade = grade;
        this.subjectType = subjectType;
    }

    public SubjectWithGrade(SubjectForStudyPlan subjectForStudyPlan, SubjectType subjectType) {
        this.subjectForStudyPlan = subjectForStudyPlan;
        this.subjectType = subjectType;
    }

    public SubjectWithGrade(Grade grade, SubjectType subjectType) {
        this.grade = grade;
        this.subjectType = subjectType;
    }

    public SubjectForStudyPlan getSubjectForStudyPlan() {
        return subjectForStudyPlan;
    }

    public void setSubjectForStudyPlan(SubjectForStudyPlan subjectForStudyPlan) {
        this.subjectForStudyPlan = subjectForStudyPlan;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public SubjectType getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(SubjectType subjectType) {
        this.subjectType = subjectType;
    }

    @Override
    public String toString() {
        return "SubjectWithGrade{" +
                "subjectForStudyPlan=" + subjectForStudyPlan +
                ", grade=" + grade +
                ", subjectType=" + subjectType +
                '}';
    }
}
