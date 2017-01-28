package at.ac.tuwien.inso.service.course_recommendation;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Student;

public interface RecommendationService {

    /**
     * Recommends courses for a student by first filtering courses, running all the course scorers,
     * normalizing the results and scaling each scorer function by weight. Finally, the list is
     * sorted by score.
     *
     * The user needs to be authenticated.
     *
     * @param student The student that needs recommendations
     * @return a sorted list of courses by weighted score
     */
    @PreAuthorize("isAuthenticated()")
    List<Course> recommendCourses(Student student);

    /**
     * Recommends courses, whereby the list contains no more than a number of entries defined as a
     * constant in the implementation.
     *
     * The user needs to be authenticated.
     *
     * @param student
     * @return A sorted list of courses
     */
    @PreAuthorize("isAuthenticated()")
    List<Course> recommendCoursesSublist(Student student);
}
