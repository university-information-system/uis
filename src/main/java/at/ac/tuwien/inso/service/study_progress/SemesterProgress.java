package at.ac.tuwien.inso.service.study_progress;

import at.ac.tuwien.inso.dto.SemesterDto;

import java.util.*;

public class SemesterProgress {

    private SemesterDto semester;

    private List<CourseRegistration> courseRegistrations;

    public SemesterProgress(SemesterDto semester, List<CourseRegistration> courseRegistrations) {
        this.semester = semester;
        this.courseRegistrations = courseRegistrations;
    }

    public SemesterDto getSemester() {
        return semester;
    }

    public List<CourseRegistration> getCourseRegistrations() {
        return courseRegistrations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SemesterProgress that = (SemesterProgress) o;

        if (getSemester() != null ? !getSemester().equals(that.getSemester()) : that.getSemester() != null)
            return false;
        return getCourseRegistrations() != null ? getCourseRegistrations().equals(that.getCourseRegistrations()) : that.getCourseRegistrations() == null;

    }

    @Override
    public int hashCode() {
        int result = getSemester() != null ? getSemester().hashCode() : 0;
        result = 31 * result + (getCourseRegistrations() != null ? getCourseRegistrations().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SemesterProgress{" +
                "semester=" + semester +
                ", courseRegistrations=" + courseRegistrations +
                '}';
    }
}
