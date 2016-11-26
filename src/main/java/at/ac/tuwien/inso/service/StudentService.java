package at.ac.tuwien.inso.service;

import org.springframework.security.access.prepost.PreAuthorize;

import at.ac.tuwien.inso.entity.*;

import java.util.*;

public interface StudentService {

    @PreAuthorize("isAuthenticated()")
    Student findOne(Long id);

    @PreAuthorize("hasRole('ADMIN')")
    List<StudyPlanRegistration> findStudyPlanRegistrationsFor(Student student);

    @PreAuthorize("hasRole('ADMIN')")
    void registerStudentToStudyPlan(Student student, StudyPlan studyPlan);

    @PreAuthorize("hasRole('ADMIN')")
    void registerStudentToStudyPlan(Student student, StudyPlan studyPlan, Semester currentSemester);
}
