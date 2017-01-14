package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.controller.lecturer.forms.*;
import at.ac.tuwien.inso.dto.*;
import at.ac.tuwien.inso.entity.*;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.*;

import javax.validation.constraints.*;
import java.util.*;

public interface CourseService {

    @PreAuthorize("isAuthenticated()")
    Page<Course> findCourseForCurrentSemesterWithName(@NotNull String name, Pageable pageable);

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

    @PreAuthorize("isAuthenticated()")
	List<Course> findCoursesForSubject(Subject subject);

    @PreAuthorize("isAuthenticated()")
    Course unregisterStudentFromCourse(Student student, Long courseId);

    @PreAuthorize("isAuthenticated()")
    CourseDetailsForStudent courseDetailsFor(Student student, Long courseId);

    @PreAuthorize("hasAnyRole('ROLE_LECTURER', 'ROLE_ADMIN')")
    List<SubjectForStudyPlan> getSubjectForStudyPlanList(Course course);
}
