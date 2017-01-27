package at.ac.tuwien.inso.service.course_recommendation;

import java.util.List;
import java.util.Map;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Student;

public interface CourseScorer {

    double weight();

    Map<Course, Double> score(List<Course> courses, Student student);
}
