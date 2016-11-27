package at.ac.tuwien.inso.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import at.ac.tuwien.inso.entity.Grade;

@Service
public interface GradeService {

    @PreAuthorize("hasRole('LECTURER')")
    Grade getDefaultGradeForStudentAndCourse(Long studentId, Long courseId);

    @PreAuthorize("hasRole('LECTURER')")
    Grade saveNewGradeForStudentAndCourse(Grade grade);

    Grade getForValidation(String identifier);

}
