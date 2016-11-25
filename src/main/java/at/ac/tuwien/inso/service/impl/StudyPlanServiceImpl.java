package at.ac.tuwien.inso.service.impl;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.exception.BusinessObjectNotFoundException;
import at.ac.tuwien.inso.exception.ValidationException;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;
import java.util.stream.*;

@Service
public class StudyPlanServiceImpl implements StudyPlanService {

    private StudyPlanRepository studyPlanRepository;
    private SubjectForStudyPlanRepository subjectForStudyPlanRepository;
    private SubjectService subjectService;
    private MessageSource messageSource;

    @Autowired
    public StudyPlanServiceImpl(
            StudyPlanRepository studyPlanRepository,
            SubjectForStudyPlanRepository subjectForStudyPlanRepository,
            SubjectService subjectService,
            MessageSource messageSource) {
        this.studyPlanRepository = studyPlanRepository;
        this.subjectForStudyPlanRepository = subjectForStudyPlanRepository;
        this.subjectService = subjectService;
        this.messageSource = messageSource;
    }

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
        StudyPlan studyPlan = studyPlanRepository.findOne(id);
        if(studyPlan == null) {
            String msg = messageSource.getMessage("error.studyplan.notfound", null, LocaleContextHolder.getLocale());
            throw new BusinessObjectNotFoundException(msg);
        }
        return studyPlan;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubjectForStudyPlan> getSubjectsForStudyPlan(Long id) {
        return subjectForStudyPlanRepository.findByStudyPlanIdOrderBySemesterRecommendation(id);
    }

    @Override
    @Transactional
    public void addSubjectToStudyPlan(SubjectForStudyPlan subjectForStudyPlan) {
        if(subjectForStudyPlan.getSubject() == null || subjectForStudyPlan.getSubject().getId() == null) {
            String msg = messageSource.getMessage("error.subject.missing", null, LocaleContextHolder.getLocale());
            throw new ValidationException(msg);
        }
        if(subjectForStudyPlan.getStudyPlan() == null || subjectForStudyPlan.getStudyPlan().getId() == null) {
            String msg = messageSource.getMessage("error.studyplan.missing", null, LocaleContextHolder.getLocale());
            throw new ValidationException(msg);
        }

        subjectForStudyPlanRepository.save(subjectForStudyPlan);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subject> getAvailableSubjectsForStudyPlan(Long id, String query) {
        List<SubjectForStudyPlan> subjectsForStudyPlan = subjectForStudyPlanRepository.findByStudyPlanIdOrderBySemesterRecommendation(id);
        List<Subject> subjectsOfStudyPlan = subjectsForStudyPlan
                .stream()
                .map(SubjectForStudyPlan::getSubject)
                .collect(Collectors.toList());
        List<Subject> subjects = subjectService.searchForSubjects(query);

        return subjects.stream().filter(it -> !subjectsOfStudyPlan.contains(it)).collect(Collectors.toList());

    }
}
