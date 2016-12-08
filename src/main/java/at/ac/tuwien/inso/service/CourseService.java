package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.controller.lecturer.forms.*;
import at.ac.tuwien.inso.entity.*;
import org.springframework.security.access.prepost.*;

import javax.validation.constraints.*;
import java.util.*;

public interface CourseService {

    @PreAuthorize("isAuthenticated()")
    List<Course> findCourseForCurrentSemesterWithName(@NotNull String name);

    @PreAuthorize("isAuthenticated()")
    List<Course> findCoursesForCurrentSemesterForLecturer(Lecturer lecturer);

    @PreAuthorize("hasAnyRole('ROLE_LECTURER', 'ROLE_ADMIN')")
    Course saveCourse(AddCourseForm form);

    @PreAuthorize("isAuthenticated()")
    Course findOne(Long id);

    @PreAuthorize("hasRole('STUDENT')")
    boolean registerStudentForCourse(Course course);

    @PreAuthorize("isAuthenticated()")
    List<Course> findAllForStudent(Student student);
}
