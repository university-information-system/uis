package at.ac.tuwien.inso.service.impl;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;
import java.util.stream.*;

@Service
public class StudyPlanServiceImpl implements StudyPlanService {

    @Autowired
    private StudyPlanRepository studyPlanRepository;

    @Autowired
    private SubjectForStudyPlanRepository subjectForStudyPlanRepository;

    @Override
    @Transactional
    public StudyPlan create(StudyPlan studyPlan) {
        return studyPlanRepository.save(studyPlan);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudyPlan> findAll() {
        Iterable<StudyPlan> studyplans = studyPlanRepository.findAll();

        return StreamSupport
                .stream(studyplans.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public StudyPlan findOne(Long id) {
        return studyPlanRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubjectForStudyPlan> getSubjectsForStudyPlan(Long id) {
        return subjectForStudyPlanRepository.findByStudyPlanIdOrderBySemesterRecommendation(id);
    }

    @Override
    @Transactional
    public void addSubjectToStudyPlan(SubjectForStudyPlan subjectForStudyPlan) {
        subjectForStudyPlanRepository.save(subjectForStudyPlan);
    }
}
