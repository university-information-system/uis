package at.ac.tuwien.inso.service.study_progress;

import at.ac.tuwien.inso.entity.*;

public class CourseRegistration {

    private Course course;

    private CourseRegistrationState state;

    public CourseRegistration(Course course) {
        this(course, CourseRegistrationState.in_progress);
    }

    public CourseRegistration(Course course, CourseRegistrationState state) {
        this.course = course;
        this.state = state;
    }

    public Course getCourse() {
        return course;
    }

    public CourseRegistrationState getState() {
        return state;
    }
}
