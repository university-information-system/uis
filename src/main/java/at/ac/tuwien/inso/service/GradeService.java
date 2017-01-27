package at.ac.tuwien.inso.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import at.ac.tuwien.inso.dto.GradeAuthorizationDTO;
import at.ac.tuwien.inso.entity.Grade;
import at.ac.tuwien.inso.entity.Mark;
import at.ac.tuwien.inso.entity.Student;

@Service
public interface GradeService {

	/**
	 * 
	 * @param studentId
	 * @param courseId
	 * @return
	 */
    @PreAuthorize("hasRole('LECTURER')")
    GradeAuthorizationDTO getDefaultGradeAuthorizationDTOForStudentAndCourse(Long studentId, Long courseId);

    @PreAuthorize("hasRole('LECTURER')")
    Grade saveNewGradeForStudentAndCourse(GradeAuthorizationDTO grade);

    @PreAuthorize("hasRole('LECTURER')")
    List<Grade> getGradesForCourseOfLoggedInLecturer(Long courseId);
    
    @PreAuthorize("hasRole('LECTURER')")
	List<Grade> findAllByCourseId(Long courseId);

    @PreAuthorize("hasRole('STUDENT')")
    List<Grade> getGradesForLoggedInStudent();

    /**
     * returns the grade for a string identifier for validation purposes
     * 
     * user needs not authentication
     * 
     * @param identifier
     * @return
     */
    Grade getForValidation(String identifier);

    /**
     * returns 
     * @param student
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    List<Grade> findAllOfStudent(Student student);

    @PreAuthorize("isAuthenticated()")
    List<Mark> getMarkOptions();

}
