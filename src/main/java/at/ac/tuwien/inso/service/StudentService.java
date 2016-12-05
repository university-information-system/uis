package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.*;
import org.springframework.security.access.prepost.*;

import java.util.*;

public interface StudentService {

    @PreAuthorize("isAuthenticated()")
    Student findOne(Long id);

    @PreAuthorize("isAuthenticated()")
    Student findOne(UserAccount account);

    @PreAuthorize("isAuthenticated()")
    Student findByUsername(String username);

    @PreAuthorize("hasRole('ADMIN')")
    List<StudyPlanRegistration> findStudyPlanRegistrationsFor(Student student);

    @PreAuthorize("hasRole('ADMIN')")
    void registerStudentToStudyPlan(Student student, StudyPlan studyPlan);

    @PreAuthorize("hasRole('ADMIN')")
    void registerStudentToStudyPlan(Student student, StudyPlan studyPlan, Semester currentSemester);
}
