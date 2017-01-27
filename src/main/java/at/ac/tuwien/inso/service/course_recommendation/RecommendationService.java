package at.ac.tuwien.inso.service.course_recommendation;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Student;

public interface RecommendationService {

    @PreAuthorize("isAuthenticated()")
    List<Course> recommendCourses(Student student);

    @PreAuthorize("isAuthenticated()")
    List<Course> recommendCoursesSublist(Student student);
}
