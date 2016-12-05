package at.ac.tuwien.inso.service.study_progress;

import at.ac.tuwien.inso.entity.*;

import java.util.*;

public class SemesterProgress {

    private Semester semester;

    private List<CourseRegistration> courseRegistrations;

    public SemesterProgress(Semester semester, List<CourseRegistration> courseRegistrations) {
        this.semester = semester;
        this.courseRegistrations = courseRegistrations;
    }

    public Semester getSemester() {
        return semester;
    }

    public List<CourseRegistration> getCourseRegistrations() {
        return courseRegistrations;
    }
}
