package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.StudyPlan;
import at.ac.tuwien.inso.repository.StudyPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudyPlanService {

    @Autowired
    private StudyPlanRepository studyPlanRepository;

    public Iterable<StudyPlan> getAllStudyPlans() {
        return studyPlanRepository.findAll();
    }
}
