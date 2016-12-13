package at.ac.tuwien.inso.service.course_recommendation;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Student;

import java.util.List;
import java.util.Map;

public interface CourseScorer {

    default double weight() {
        return 1;
    }

    Map<Course, Double> score(List<Course> courses, Student student);
}
