package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.StudyPlan;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.entity.SubjectForStudyPlan;
import at.ac.tuwien.inso.repository.StudyPlanRepository;
import at.ac.tuwien.inso.repository.SubjectForStudyPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudyPlanService {

    @Autowired
    private StudyPlanRepository studyPlanRepository;

    @Autowired
    private SubjectForStudyPlanRepository subjectForStudyPlanRepository;

    public Iterable<StudyPlan> getAllStudyPlans() {
        return studyPlanRepository.findAll();
    }

    public StudyPlan getStudyPlanById(Long id) {
        return studyPlanRepository.findStudyPlanById(id);
    }

    public Iterable<SubjectForStudyPlan> getSubjectsForStudyPlan(Long id) {
        return subjectForStudyPlanRepository.findByStudyPlanIdOrderBySemesterRecommendation(id);
    }
}
