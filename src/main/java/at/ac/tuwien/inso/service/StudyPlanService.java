package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.*;

import java.util.*;

public interface StudyPlanService {

    StudyPlan create(StudyPlan studyPlan);

    List<StudyPlan> findAll();

    StudyPlan findOne(Long id);

    List<SubjectForStudyPlan> getSubjectsForStudyPlan(Long id);

    void addSubjectToStudyPlan(SubjectForStudyPlan subjectForStudyPlan);
}
