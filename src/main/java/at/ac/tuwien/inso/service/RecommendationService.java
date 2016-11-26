package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Student;

import java.util.List;

public interface RecommendationService {

    List<Course> recommendCourses(Student student);
}
