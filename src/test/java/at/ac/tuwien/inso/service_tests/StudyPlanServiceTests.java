package at.ac.tuwien.inso.service_tests;

import at.ac.tuwien.inso.entity.EctsDistribution;
import at.ac.tuwien.inso.entity.StudyPlan;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.entity.SubjectForStudyPlan;
import at.ac.tuwien.inso.exception.BusinessObjectNotFoundException;
import at.ac.tuwien.inso.exception.ValidationException;
import at.ac.tuwien.inso.repository.StudyPlanRepository;
import at.ac.tuwien.inso.repository.SubjectForStudyPlanRepository;
import at.ac.tuwien.inso.service.GradeService;
import at.ac.tuwien.inso.service.SubjectService;
import at.ac.tuwien.inso.service.impl.StudyPlanServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("test")
public class StudyPlanServiceTests {

    @Mock
    private StudyPlanRepository studyPlanRepository;

    @Mock
    private SubjectForStudyPlanRepository subjectForStudyPlanRepository;

    @Mock
    private SubjectService subjectService;

    @Mock
    private GradeService gradeService;

    @Mock
    private MessageSource messageSource;

    private StudyPlanServiceImpl studyPlanService;

    private static final Long VALID_STUDY_PLAN_ID = 1L;
    private static final Long INVALID_STUDY_PLAN_ID = 1337L;
    private StudyPlan studyPlan = new StudyPlan("sp", new EctsDistribution(new BigDecimal(60), new BigDecimal(60), new BigDecimal(60)));
    private SubjectForStudyPlan subjectForStudyPlan = new SubjectForStudyPlan(new Subject("subject", null),studyPlan, false);
    private List<Subject> subjects = new ArrayList<>();
    private List<SubjectForStudyPlan> subjectsForStudyPlan = new ArrayList<>();

    @Before
    public void setUp() {
        subjects.addAll(asList(
                new Subject("VU Programmkonstruktion", new BigDecimal(8.8)),
                new Subject("UE Studieneingangsgespräch", new BigDecimal(0.2)),
                new Subject("VU Technische Grundlagen der Informatik", new BigDecimal(6.0)),
                new Subject("VO Algebra und Diskrete Mathematik für Informatik und Wirtschaftsinformatik", new BigDecimal(4.0)),
                new Subject("UE Algebra und Diskrete Mathematik für Informatik und Wirtschaftsinformatik", new BigDecimal(5.0)),
                new Subject("VU Formale Modellierung", new BigDecimal(3.0)),
                new Subject("VU Datenmodellierung", new BigDecimal(3.0))
        ));
        subjectsForStudyPlan.addAll(asList(
                new SubjectForStudyPlan(subjects.get(0), studyPlan, true),
                new SubjectForStudyPlan(subjects.get(2), studyPlan, true),
                new SubjectForStudyPlan(subjects.get(4), studyPlan, true),
                new SubjectForStudyPlan(subjects.get(5), studyPlan, true),
                new SubjectForStudyPlan(subjects.get(6), studyPlan, true)
        ));

        MockitoAnnotations.initMocks(this);
        when(studyPlanRepository.findOne(VALID_STUDY_PLAN_ID)).thenReturn(studyPlan);
        when(studyPlanRepository.findOne(INVALID_STUDY_PLAN_ID)).thenReturn(null);
        when(subjectForStudyPlanRepository.save(subjectForStudyPlan)).thenReturn(subjectForStudyPlan);
        when(subjectService.searchForSubjects(any())).thenReturn(subjects);
        when(subjectForStudyPlanRepository.findByStudyPlanIdOrderBySemesterRecommendation(VALID_STUDY_PLAN_ID)).thenReturn(subjectsForStudyPlan);
        studyPlanService = new StudyPlanServiceImpl(studyPlanRepository, subjectForStudyPlanRepository, subjectService, gradeService, messageSource);
    }

    @Test
    public void getAvailableSubjectsForStudyPlanShouldReturnAvailableSubjects() {
        List<Subject> availableSubjects = studyPlanService.getAvailableSubjectsForStudyPlan(VALID_STUDY_PLAN_ID, "some query");
        assertEquals(asList(subjects.get(1), subjects.get(3)),availableSubjects);
    }

    @Test
    public void findOneWithValidIdShouldReturnStudyPlanTest() {
        StudyPlan actualStudyPlan = studyPlanService.findOne(VALID_STUDY_PLAN_ID);
        assertEquals(studyPlan, actualStudyPlan);
    }

    @Test(expected = BusinessObjectNotFoundException.class)
    public void findOneWithInvalidIdShouldThrowExceptionTest() {
        studyPlanService.findOne(INVALID_STUDY_PLAN_ID);
    }

    @Test(expected = ValidationException.class)
    public void addSubjectToStudyPlanSubjectNullTShouldThrowExceptionTest() {
        subjectForStudyPlan.setSubject(null);
        studyPlanService.addSubjectToStudyPlan(subjectForStudyPlan);
    }

    @Test(expected = ValidationException.class)
    public void addSubjectToStudyPlanSubjectIdNullTShouldThrowExceptionTest() {
        subjectForStudyPlan.getSubject().setId(null);
        studyPlanService.addSubjectToStudyPlan(subjectForStudyPlan);
    }

    @Test(expected = ValidationException.class)
    public void addSubjectToStudyPlanStudyPlanNullTShouldThrowExceptionTest() {
        subjectForStudyPlan.setStudyPlan(null);
        studyPlanService.addSubjectToStudyPlan(subjectForStudyPlan);
    }

    @Test(expected = ValidationException.class)
    public void addSubjectToStudyPlanStudyPlanIdNullTShouldThrowExceptionTest() {
        subjectForStudyPlan.getStudyPlan().setId(null);
        studyPlanService.addSubjectToStudyPlan(subjectForStudyPlan);
    }

    /**
     * @author m.pazourek
     */
    @Test(expected = ValidationException.class)
    public void removeSubjectFromStudyPlanSubjectIdNotNullTest(){
     
      subjectForStudyPlan.getStudyPlan().setId(null);
      Subject subject = subjects.get(0);
      
      studyPlanService.removeSubjectFromStudyPlan(subjectForStudyPlan.getStudyPlan(), subject);
    }
    
    @Test(expected = ValidationException.class)
    public void removeSubjectsFromStudyPlanStudyPlanIdNotNullTest(){

      Subject subject = subjects.get(0);
      subject.setId(null);
      
      studyPlanService.removeSubjectFromStudyPlan(subjectForStudyPlan.getStudyPlan(), subject);
    }
}
