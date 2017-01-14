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

import at.ac.tuwien.inso.dto.SemesterDto;
import at.ac.tuwien.inso.entity.EctsDistribution;
import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.entity.SemesterType;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.StudyPlan;
import at.ac.tuwien.inso.repository.SemesterRepository;
import at.ac.tuwien.inso.repository.StudyPlanRepository;
import at.ac.tuwien.inso.service.StudentService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class StudentServiceSecurityTests {

    @Autowired
    private StudentService studentService;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private StudyPlanRepository studyPlanRepository;

    private SemesterDto semester;
    private StudyPlan studyPlan;

    @Before
    public void setUp() {
        Semester localSemester = new Semester(2016, SemesterType.WinterSemester);

        semester = semesterRepository.save(localSemester).toDto();

        StudyPlan localStudyPlan = new StudyPlan("TestStudyPlan",
                new EctsDistribution(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE));

        studyPlan = studyPlanRepository.save(localStudyPlan);
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void findOneNotAuthenticated() {
        studentService.findOne(Long.valueOf(1));
    }

    @Test
    @WithMockUser
    public void findOneAuthenticated() {
        studentService.findOne(Long.valueOf(1));
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void registerStudentToStudyPlanNotAuthenticated() {
        studentService.registerStudentToStudyPlan(
                new Student("student", "student", "student@student.com"),
                studyPlan);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "STUDENT")
    public void registerStudentToStudyPlanAuthenticatedAsStudent() {
        studentService.registerStudentToStudyPlan(
                new Student("student", "student", "student@student.com"),
                studyPlan);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "LECTURER")
    public void registerStudentToStudyPlanAuthenticatedAsLecturer() {
        studentService.registerStudentToStudyPlan(
                new Student("student", "student", "student@student.com"),
                studyPlan);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void registerStudentToStudyPlanAuthenticatedAsAdmin() {
        studentService.registerStudentToStudyPlan(
                new Student("student", "student", "student@student.com"),
                studyPlan);
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void registerStudentToStudyPlanVersion2NotAuthenticated() {
        studentService.registerStudentToStudyPlan(
                new Student("student", "student", "student@student.com"),
                studyPlan,
                semester);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "STUDENT")
    public void registerStudentToStudyPlanVersion2AuthenticatedAsStudent() {
        studentService.registerStudentToStudyPlan(
                new Student("student", "student", "student@student.com"),
                studyPlan,
                semester);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "LECTURER")
    public void registerStudentToStudyPlanVersion2AuthenticatedAsLecturer() {
        studentService.registerStudentToStudyPlan(
                new Student("student", "student", "student@student.com"),
                studyPlan,
                semester);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void registerStudentToStudyPlanVersion2AuthenticatedAsAdmin() {
        studentService.registerStudentToStudyPlan(
                new Student("student", "student", "student@student.com"),
                studyPlan,
                semester);
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void findStudyPlanRegistrationsForNotAuthenticated() {
        studentService.findStudyPlanRegistrationsFor(
                new Student("student", "student", "student@student.com"));
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "STUDENT")
    public void findStudyPlanRegistrationsForAuthenticatedAsStudent() {
        studentService.findStudyPlanRegistrationsFor(
                new Student("student", "student", "student@student.com"));
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "LECTURER")
    public void findStudyPlanRegistrationsForAuthenticatedAsLecturer() {
        studentService.findStudyPlanRegistrationsFor(
                new Student("student", "student", "student@student.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void findStudyPlanRegistrationsForAuthenticatedAsAdmin() {
        studentService.findStudyPlanRegistrationsFor(
                new Student("student", "student", "student@student.com"));
    }

}
