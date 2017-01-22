package at.ac.tuwien.inso.service.course_recommendation;

import at.ac.tuwien.inso.entity.*;

import java.util.*;

public interface CourseScorer {

    double weight();

    Map<Course, Double> score(List<Course> courses, Student student);
}
