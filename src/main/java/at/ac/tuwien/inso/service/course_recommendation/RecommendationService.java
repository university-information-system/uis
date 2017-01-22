package at.ac.tuwien.inso.service.course_recommendation;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Student;

import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface RecommendationService {

    @PreAuthorize("isAuthenticated()")
    List<Course> recommendCourses(Student student);

    @PreAuthorize("isAuthenticated()")
    List<Course> recommendCoursesSublist(Student student);
}
