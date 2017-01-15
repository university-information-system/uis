package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.dto.GradeAuthorizationDTO;
import at.ac.tuwien.inso.entity.*;
import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public interface GradeService {

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

    Grade getForValidation(String identifier);

    @PreAuthorize("isAuthenticated()")
    List<Grade> findAllOfStudent(Student student);

    @PreAuthorize("isAuthenticated()")
    List<Mark> getMarkOptions();

}
