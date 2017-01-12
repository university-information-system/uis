package at.ac.tuwien.inso.service.impl;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.exception.BusinessObjectNotFoundException;
import at.ac.tuwien.inso.exception.ValidationException;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.*;
import at.ac.tuwien.inso.service.validator.StudyPlanValidator;
import at.ac.tuwien.inso.service.validator.ValidatorFactory;
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
        validator.validateNewStudyPlan(studyPlan);
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
        validator.validateStudyPlanId(id);
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
        validator.validateStudyPlanId(id);
        return subjectForStudyPlanRepository.findByStudyPlanIdOrderBySemesterRecommendation(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubjectWithGrade> getSubjectsWithGradesForStudyPlan(Long id) {
        validator.validateStudyPlanId(id);
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

    /**
     * @author m.pazourek
     * disables a studyplan by the given id
     */
    @Override
    public StudyPlan disableStudyPlan(Long id) {
      StudyPlan studyPlan = findOne(id);
      studyPlan.setEnabled(false);
      studyPlanRepository.save(studyPlan);
        return studyPlan;
    }

    /**
     * @author m.pazourek
     * removes a subject from the studyplan
     */
    @Override
    public void removeSubjectFromStudyPlan(StudyPlan sp, Subject s) {
      //System.out.println(sp.getId()+", "+s.getId());
      if(sp==null||sp.getId()==null||s==null||s.getId()==null){
        throw new ValidationException();
      }
      
      List<SubjectForStudyPlan> sfsp = subjectForStudyPlanRepository.findByStudyPlanIdOrderBySemesterRecommendation(sp.getId());
      for(SubjectForStudyPlan each:sfsp){
        if(each.getSubject().getId() == s.getId()){
          subjectForStudyPlanRepository.delete(each);
        }
      }      
    }
}
