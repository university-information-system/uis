package at.ac.tuwien.inso.service.study_progress;

import java.util.List;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Grade;

public class CourseRegistration {

    private Course course;

    private CourseRegistrationState state;

	private Grade grade;

    public CourseRegistration(Course course) {
        this(course, CourseRegistrationState.in_progress);
    }

    public CourseRegistration(Course course, CourseRegistrationState state) {
        this.course = course;
        this.state = state;
    }
    
    public CourseRegistration(Course course, CourseRegistrationState state, List<Grade> grade) {
        this.course = course;
        this.state = state;
        if(grade!=null&&!grade.isEmpty()){
        	this.grade = grade.get(0);
        }
    }

    public Course getCourse() {
        return course;
    }

    public CourseRegistrationState getState() {
        return state;
    }
    
    public Grade getGrade(){
    	return grade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CourseRegistration that = (CourseRegistration) o;

        if (getCourse() != null ? !getCourse().equals(that.getCourse()) : that.getCourse() != null) return false;
        return getState() == that.getState();

    }

    @Override
    public int hashCode() {
        int result = getCourse() != null ? getCourse().hashCode() : 0;
        result = 31 * result + (getState() != null ? getState().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CourseRegistration{" +
                "course=" + course +
                ", state=" + state +
                '}';
    }
}
