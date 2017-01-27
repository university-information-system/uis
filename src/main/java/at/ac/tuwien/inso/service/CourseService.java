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
import at.ac.tuwien.inso.exception.BusinessObjectNotFoundException;
import at.ac.tuwien.inso.exception.ValidationException;

public interface CourseService {

	/**
	 * this method returns all courses for the current semester with the specified name of a subject 
	 * may start a new semester!
	 * the user needs to be authenticated
	 * 
	 * @param name the name of a subject to search for, search strategy is NameLikeIgnoreCase
	 * @param pageable a spring pageable element
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	Page<Course> findCourseForCurrentSemesterWithName(@NotNull String name, Pageable pageable);

	/**
	 * returns all courses of the given lecturer for the current semester 
	 * may start a new semester!
	 * the user needs to be authenticated
	 * 
	 * @param lecturer
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	List<Course> findCoursesForCurrentSemesterForLecturer(Lecturer lecturer);

	/**
	 * this method saves a new course by the given AddCourseForm
	 * this method should also take care of tags that are contained by the form. if they are new and have not been in the system before, they should be created
	 * the user needs to be lecturer or admin
	 * 
	 * @param form
	 * @return
	 */
	@PreAuthorize("hasAnyRole('ROLE_LECTURER', 'ROLE_ADMIN')")
	Course saveCourse(AddCourseForm form);

	/**
	 * this method returns a course by the given id. 
	 * may throw a BusinessObjectNotFoundException if there is no course with the given id
	 * @param id the id should not be null and not <1
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	Course findOne(Long id);

	/**
	 * this method removes a course by its id if there are not registerd students or grades for it 
	 * @param id should not be null and not <1
	 * @return
	 * @throws ValidationException if there are grades or already registered students for that course
	 * @throws BusinessObjectNotFoundException if there is not course with the given id
	 */
	@PreAuthorize("hasAnyRole('ROLE_LECTURER', 'ROLE_ADMIN')")
	boolean remove(Long id) throws ValidationException;

	@PreAuthorize("hasRole('STUDENT')")
	boolean registerStudentForCourse(Course course);

	@PreAuthorize("isAuthenticated()")
	List<Course> findAllForStudent(Student student);

	@PreAuthorize("isAuthenticated()")
	List<Course> findCoursesForSubject(Subject subject);

	@PreAuthorize("isAuthenticated()")
	List<Course> findCoursesForSubjectAndCurrentSemester(Subject subject);

	@PreAuthorize("hasRole('ROLE_STUDENT')")
	void dismissCourse(Student student, Long courseId);

	@PreAuthorize("isAuthenticated()")
	Course unregisterStudentFromCourse(Student student, Long courseId);

	@PreAuthorize("isAuthenticated()")
	CourseDetailsForStudent courseDetailsFor(Student student, Long courseId);

	@PreAuthorize("hasAnyRole('ROLE_LECTURER', 'ROLE_ADMIN')")
	List<SubjectForStudyPlan> getSubjectForStudyPlanList(Course course);

	@PreAuthorize("isAuthenticated()")
	List<Course> findAllCoursesForCurrentSemester();
}
