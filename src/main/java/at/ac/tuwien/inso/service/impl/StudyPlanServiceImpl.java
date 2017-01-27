package at.ac.tuwien.inso.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.inso.entity.Grade;
import at.ac.tuwien.inso.entity.StudyPlan;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.entity.SubjectForStudyPlan;
import at.ac.tuwien.inso.entity.SubjectType;
import at.ac.tuwien.inso.entity.SubjectWithGrade;
import at.ac.tuwien.inso.exception.BusinessObjectNotFoundException;
import at.ac.tuwien.inso.repository.StudyPlanRepository;
import at.ac.tuwien.inso.repository.SubjectForStudyPlanRepository;
import at.ac.tuwien.inso.service.GradeService;
import at.ac.tuwien.inso.service.StudyPlanService;
import at.ac.tuwien.inso.service.SubjectService;
import at.ac.tuwien.inso.service.validator.StudyPlanValidator;
import at.ac.tuwien.inso.service.validator.ValidatorFactory;

@Service
public class StudyPlanServiceImpl implements StudyPlanService {

	private static final Logger log = LoggerFactory.getLogger(StudyPlanServiceImpl.class);

    private StudyPlanRepository studyPlanRepository;
    private SubjectForStudyPlanRepository subjectForStudyPlanRepository;
    private SubjectService subjectService;
    private MessageSource messageSource;
    private GradeService gradeService;
    private ValidatorFactory validatorFactory = new ValidatorFactory();
    private StudyPlanValidator validator = validatorFactory.getStudyPlanValidator();

    @Autowired
    public StudyPlanServiceImpl(
            StudyPlanRepository studyPlanRepository,
            SubjectForStudyPlanRepository subjectForStudyPlanRepository,
            SubjectService subjectService,
            GradeService gradeService,
            MessageSource messageSource) {
        this.studyPlanRepository = studyPlanRepository;
        this.subjectForStudyPlanRepository = subjectForStudyPlanRepository;
        this.subjectService = subjectService;
        this.gradeService = gradeService;
        this.messageSource = messageSource;
    }

    @Override
    @Transactional
    public StudyPlan create(StudyPlan studyPlan) {
    	log.info("creating studyplan "+studyPlan.toString());
    	validator.validateNewStudyPlan(studyPlan);
        return studyPlanRepository.save(studyPlan);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudyPlan> findAll() {
    	log.info("getting all studyplans");
        Iterable<StudyPlan> studyplans = studyPlanRepository.findAll();

        return StreamSupport
                .stream(studyplans.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public StudyPlan findOne(Long id) {
    	log.info("trying to find one studyplan by id "+id);
        validator.validateStudyPlanId(id);
        StudyPlan studyPlan = studyPlanRepository.findOne(id);
        if(studyPlan == null) {
            String msg = messageSource.getMessage("error.studyplan.notfound", null, LocaleContextHolder.getLocale());
            log.warn("no studyplan was found by the given id "+id);
            throw new BusinessObjectNotFoundException(msg);
        }
        return studyPlan;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubjectForStudyPlan> getSubjectsForStudyPlan(Long id) {
        validator.validateStudyPlanId(id);
    	log.info("get subjects for studypolan by id "+id);
        return subjectForStudyPlanRepository.findByStudyPlanIdOrderBySemesterRecommendation(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubjectWithGrade> getSubjectsWithGradesForStudyPlan(Long id) {
        validator.validateStudyPlanId(id);
    	log.info("getting subjects with grades for studyplan width id "+id);
        List<SubjectForStudyPlan> subjectsForStudyPlan = subjectForStudyPlanRepository.findByStudyPlanIdOrderBySemesterRecommendation(id);
        List<Grade> grades = gradeService.getGradesForLoggedInStudent();
        List<SubjectWithGrade> subjectsWithGrades = new ArrayList<>();
        
        for(SubjectForStudyPlan subjectForStudyPlan : subjectsForStudyPlan) {

            if(grades.isEmpty()) {
                // means there are no (more) grades at all
                if (subjectForStudyPlan.getMandatory()) {
                    subjectsWithGrades.add(new SubjectWithGrade(subjectForStudyPlan, SubjectType.MANDATORY));
                } else {
                    subjectsWithGrades.add(new SubjectWithGrade(subjectForStudyPlan, SubjectType.OPTIONAL));
                }
            }

            //look for grades belonging to the actual subject
            for(int i=0; i<grades.size(); i++) {
                Grade grade = grades.get(i);
                if(grade.getCourse().getSubject().equals(subjectForStudyPlan.getSubject())) {
                    // add to mandatory or optional subjects
                    if(subjectForStudyPlan.getMandatory()) {
                        subjectsWithGrades.add(new SubjectWithGrade(subjectForStudyPlan, grade, SubjectType.MANDATORY));
                    }
                    else {
                        subjectsWithGrades.add(new SubjectWithGrade(subjectForStudyPlan, grade, SubjectType.OPTIONAL));
                    }
                    grades.remove(grade);
                    break;
                }
                else if(i == grades.size()-1) {
                    // means we reached the end of the list. there is no grade for this subject
                    if(subjectForStudyPlan.getMandatory()) {
                        subjectsWithGrades.add(new SubjectWithGrade(subjectForStudyPlan, SubjectType.MANDATORY));
                    }
                    else {
                        subjectsWithGrades.add(new SubjectWithGrade(subjectForStudyPlan, SubjectType.OPTIONAL));
                    }
                }
            }
        }

        //remaining unassigned grades are used as free choice subjects
        for(Grade grade : grades) {
            subjectsWithGrades.add(new SubjectWithGrade(grade, SubjectType.FREE_CHOICE));
        }

        return subjectsWithGrades;
    }

    @Override
    @Transactional
    public void addSubjectToStudyPlan(SubjectForStudyPlan subjectForStudyPlan) {

        validator.validateNewSubjectForStudyPlan(subjectForStudyPlan);
        log.info("adding subject to studyplan with "+subjectForStudyPlan);

        subjectForStudyPlanRepository.save(subjectForStudyPlan);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subject> getAvailableSubjectsForStudyPlan(Long id, String query) {
    	log.info("getting available subjects for studyplan with id "+id+" and search word "+query);
    	validator.validateStudyPlanId(id);
    	List<SubjectForStudyPlan> subjectsForStudyPlan = subjectForStudyPlanRepository.findByStudyPlanIdOrderBySemesterRecommendation(id);
        List<Subject> subjectsOfStudyPlan = subjectsForStudyPlan
                .stream()
                .map(SubjectForStudyPlan::getSubject)
                .collect(Collectors.toList());
        List<Subject> subjects = subjectService.searchForSubjects(query);

        return subjects.stream().filter(it -> !subjectsOfStudyPlan.contains(it)).collect(Collectors.toList());

    }

    @Override
    @Transactional
    public StudyPlan disableStudyPlan(Long id) {

    	log.info("disabling study plan with id "+id);
        validator.validateStudyPlanId(id);
        StudyPlan studyPlan = findOne(id);
        if(studyPlan == null) {
            String msg = messageSource.getMessage("error.studyplan.notfound", null, LocaleContextHolder.getLocale());
            log.warn(msg);
            throw new BusinessObjectNotFoundException(msg);
        }
        studyPlan.setEnabled(false);
        studyPlanRepository.save(studyPlan);
        return studyPlan;
    }

    @Override
    @Transactional
    public void removeSubjectFromStudyPlan(StudyPlan sp, Subject s) {
    	validator.validateRemovingSubjectFromStudyPlan(sp, s);
    	log.info("removing subject "+s.toString()+" from studyplan "+sp.getName());
            	
        List<SubjectForStudyPlan> sfsp = subjectForStudyPlanRepository.findByStudyPlanIdOrderBySemesterRecommendation(sp.getId());
        for(SubjectForStudyPlan each : sfsp){
            if(each.getSubject().getId().equals(s.getId())){
                subjectForStudyPlanRepository.delete(each);
                break;
            }
        }
    }
}
