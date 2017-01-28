package at.ac.tuwien.inso.service_tests.course_recommendation.filters;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Student;

@Component
public class DismissedCoursesFilter implements CourseRelevanceFilter {

    @Override
    public List<Course> filter(List<Course> courses, Student student) {
        return courses.stream().filter(course -> !student.getDismissedCourses().contains(course)).collect(Collectors.toList());
    }
}
