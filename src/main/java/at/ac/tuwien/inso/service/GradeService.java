package at.ac.tuwien.inso.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

import at.ac.tuwien.inso.entity.Grade;

@Service
public interface GradeService {

    @PreAuthorize("hasRole('LECTURER')")
    Grade getDefaultGradeForStudentAndCourse(Long studentId, Long courseId);

    @PreAuthorize("hasRole('LECTURER')")
    Grade saveNewGradeForStudentAndCourse(Grade grade);

    @PreAuthorize("hasRole('STUDENT')")
    List<Grade> getGradesForLoggedInStudent();

    Grade getForValidation(String identifier);

}
