package at.ac.tuwien.inso.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import at.ac.tuwien.inso.controller.lecturer.forms.AddCourseForm;
import at.ac.tuwien.inso.dto.CourseDetailsForStudent;
import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.entity.SubjectForStudyPlan;
import at.ac.tuwien.inso.exception.ValidationException;

public interface CourseService {

    @PreAuthorize("isAuthenticated()")
    Page<Course> findCourseForCurrentSemesterWithName(@NotNull String name, Pageable pageable);

    @PreAuthorize("isAuthenticated()")
    List<Course> findCoursesForCurrentSemesterForLecturer(Lecturer lecturer);

    @PreAuthorize("hasAnyRole('ROLE_LECTURER', 'ROLE_ADMIN')")
    Course saveCourse(AddCourseForm form);

    @PreAuthorize("isAuthenticated()")
    Course findOne(Long id);
    
    @PreAuthorize("hasAnyRole('ROLE_LECTURER', 'ROLE_ADMIN')")
    boolean remove(Long id) throws ValidationException;

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
