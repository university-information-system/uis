package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.*;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public interface StudyPlanService {

    @PreAuthorize("hasRole('ADMIN')")
    StudyPlan create(StudyPlan studyPlan);

    @PreAuthorize("isAuthenticated()")
    List<StudyPlan> findAll();

    @PreAuthorize("isAuthenticated()")
    StudyPlan findOne(Long id);

    @PreAuthorize("isAuthenticated()")
    List<SubjectForStudyPlan> getSubjectsForStudyPlan(Long id);

    @PreAuthorize("hasRole('ADMIN')")
    void addSubjectToStudyPlan(SubjectForStudyPlan subjectForStudyPlan);

    @PreAuthorize("isAuthenticated()")
    List<Subject> getAvailableSubjectsForStudyPlan(Long id, String query);
}
