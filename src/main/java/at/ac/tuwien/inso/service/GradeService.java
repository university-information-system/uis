package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.*;
import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public interface GradeService {

    @PreAuthorize("hasRole('LECTURER')")
    Grade getDefaultGradeForStudentAndCourse(Long studentId, Long courseId);

    @PreAuthorize("hasRole('LECTURER')")
    Grade saveNewGradeForStudentAndCourse(Grade grade);

    @PreAuthorize("hasRole('STUDENT')")
    List<Grade> getGradesForLoggedInStudent();

    Grade getForValidation(String identifier);

    @PreAuthorize("isAuthenticated()")
    List<Grade> findAllOfStudent(Student student);
}
