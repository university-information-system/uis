package at.ac.tuwien.inso.method_security_tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import at.ac.tuwien.inso.entity.EctsDistribution;
import at.ac.tuwien.inso.entity.StudyPlan;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.entity.SubjectForStudyPlan;
import at.ac.tuwien.inso.exception.BusinessObjectNotFoundException;
import at.ac.tuwien.inso.exception.ValidationException;
import at.ac.tuwien.inso.repository.StudyPlanRepository;
import at.ac.tuwien.inso.service.StudyPlanService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class StudyPlanServiceSecurityTests {

    @Autowired
    private StudyPlanService studyPlanService;

    @Autowired
    private StudyPlanRepository studyPlanRepository;


    private StudyPlan studyPlan;

    @Before
    public void setUp() {
        studyPlan = studyPlanRepository.save(new StudyPlan("TestStudyPlan",
                new EctsDistribution(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE)));
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void createNotAuthenticated() {
        studyPlanService.create(studyPlan);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "STUDENT")
    public void createNotAuthenticatedAsStudent() {
        studyPlanService.create(studyPlan);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "LECTURER")
    public void createNotAuthenticatedAsLecturer() {
        studyPlanService.create(studyPlan);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void createNotAuthenticatedAsAdmin() {
        studyPlanService.create(studyPlan);
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void findAllNotAuthenticated() {
        studyPlanService.findAll();
    }

    @Test
    @WithMockUser
    public void findAllAuthenticated() {
        studyPlanService.findAll();
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void findOneNotAuthenticated() {
        studyPlanService.findOne(Long.valueOf(1));
    }

    @Test(expected = BusinessObjectNotFoundException.class)
    @WithMockUser
    public void findOneAuthenticated() {
        studyPlanService.findOne(Long.valueOf(1));
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void getSubjectsForStudyPlanNotAuthenticated() {
        studyPlanService.getSubjectsForStudyPlan(Long.valueOf(1));
    }

    @Test
    @WithMockUser
    public void getSubjectsForStudyPlanAuthenticated() {
        studyPlanService.getSubjectsForStudyPlan(Long.valueOf(1));
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void getAvailableSubjectsForStudyPlanNotAuthenticated() {
        studyPlanService.getAvailableSubjectsForStudyPlan(Long.valueOf(1), "");
    }

    @Test
    @WithMockUser
    public void getAvailableSubjectsForStudyPlanAuthenticated() {
        studyPlanService.getAvailableSubjectsForStudyPlan(Long.valueOf(1), "");
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void addSubjectToStudyPlanAuthenticated() {
        studyPlanService.addSubjectToStudyPlan(
                new SubjectForStudyPlan(new Subject(), studyPlan, true));
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "STUDENT")
    public void addSubjectToStudyPlanAsStudent() {
        studyPlanService.addSubjectToStudyPlan(
                new SubjectForStudyPlan(new Subject(), studyPlan, true));
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "LECTURER")
    public void addSubjectToStudyPlanAsLecturer() {
        studyPlanService.addSubjectToStudyPlan(
                new SubjectForStudyPlan(new Subject(), studyPlan, true));
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(roles = "ADMIN")
    public void addSubjectToStudyPlanAsAdmin() {
        studyPlanService.addSubjectToStudyPlan(
                new SubjectForStudyPlan(new Subject(), studyPlan, true));
    }

}
