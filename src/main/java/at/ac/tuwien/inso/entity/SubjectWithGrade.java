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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubjectWithGrade that = (SubjectWithGrade) o;

        if (subjectForStudyPlan != null ? !subjectForStudyPlan.equals(that.subjectForStudyPlan) : that.subjectForStudyPlan != null)
            return false;
        if (grade != null ? !grade.equals(that.grade) : that.grade != null) return false;
        return subjectType == that.subjectType;

    }

    @Override
    public int hashCode() {
        int result = subjectForStudyPlan != null ? subjectForStudyPlan.hashCode() : 0;
        result = 31 * result + (grade != null ? grade.hashCode() : 0);
        result = 31 * result + subjectType.hashCode();
        return result;
    }
}
