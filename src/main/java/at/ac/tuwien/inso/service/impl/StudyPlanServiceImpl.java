package at.ac.tuwien.inso.service.impl;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.exception.BusinessObjectNotFoundException;
import at.ac.tuwien.inso.exception.ValidationException;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;
import java.util.stream.*;

@Service
public class StudyPlanServiceImpl implements StudyPlanService {

	private static final Logger log = LoggerFactory.getLogger(StudyPlanServiceImpl.class);

    private StudyPlanRepository studyPlanRepository;
    private SubjectForStudyPlanRepository subjectForStudyPlanRepository;
    private SubjectService subjectService;
    private MessageSource messageSource;
    private GradeService gradeService;

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
    	log.info("get subjects for studypolan by id "+id);
        return subjectForStudyPlanRepository.findByStudyPlanIdOrderBySemesterRecommendation(id);
    }

    @Override
    public List<SubjectWithGrade> getSubjectsWithGradesForStudyPlan(Long id) {
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
    	log.info("adding subject to studyplan with "+subjectForStudyPlan);
        if(subjectForStudyPlan.getSubject() == null || subjectForStudyPlan.getSubject().getId() == null) {
            String msg = messageSource.getMessage("error.subject.missing", null, LocaleContextHolder.getLocale());
            log.warn(msg);
            throw new ValidationException(msg);
        }
        if(subjectForStudyPlan.getStudyPlan() == null || subjectForStudyPlan.getStudyPlan().getId() == null) {
            String msg = messageSource.getMessage("error.studyplan.missing", null, LocaleContextHolder.getLocale());
            log.warn(msg);
            throw new ValidationException(msg);
        }

        subjectForStudyPlanRepository.save(subjectForStudyPlan);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subject> getAvailableSubjectsForStudyPlan(Long id, String query) {
    	log.info("getting available subjects for studyplan with id "+id+" and search word "+query);
        List<SubjectForStudyPlan> subjectsForStudyPlan = subjectForStudyPlanRepository.findByStudyPlanIdOrderBySemesterRecommendation(id);
        List<Subject> subjectsOfStudyPlan = subjectsForStudyPlan
                .stream()
                .map(SubjectForStudyPlan::getSubject)
                .collect(Collectors.toList());
        List<Subject> subjects = subjectService.searchForSubjects(query);

        return subjects.stream().filter(it -> !subjectsOfStudyPlan.contains(it)).collect(Collectors.toList());

    }

    /**
     * @author m.pazourek
     * disables a studyplan by the given id
     */
    @Override
    public void disableStudyPlan(Long id) {
    	log.info("disabling study plan with id "+id);
      StudyPlan studyPlan = findOne(id);
      studyPlan.setEnabled(false);
      studyPlanRepository.save(studyPlan);
    }

    /**
     * @author m.pazourek
     * removes a subject from the studyplan
     */
    @Override
    public void removeSubjectFromStudyPlan(StudyPlan sp, Subject s) {
     
      if(sp==null||sp.getId()==null||s==null||s.getId()==null){
    	log.warn("failed removing subject from studyplan because there was a missing object or id");
        throw new ValidationException();
      }
      log.info("removing subject "+s.getName()+" from studyplan "+sp.getName());
      
      List<SubjectForStudyPlan> sfsp = subjectForStudyPlanRepository.findByStudyPlanIdOrderBySemesterRecommendation(sp.getId());
      for(SubjectForStudyPlan each:sfsp){
        if(each.getSubject().getId() == s.getId()){
          subjectForStudyPlanRepository.delete(each);
        }
      }      
    }
}
