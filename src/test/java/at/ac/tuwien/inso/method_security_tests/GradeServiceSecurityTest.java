package at.ac.tuwien.inso.method_security_tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Grade;
import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.entity.Role;
import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.entity.UserAccount;
import at.ac.tuwien.inso.repository.CourseRepository;
import at.ac.tuwien.inso.repository.LecturerRepository;
import at.ac.tuwien.inso.repository.SemesterRepository;
import at.ac.tuwien.inso.repository.StudentRepository;
import at.ac.tuwien.inso.repository.SubjectRepository;
import at.ac.tuwien.inso.service.GradeService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class GradeServiceSecurityTest {

    @Autowired
    private GradeService gradeService;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private StudentRepository studentRepository;

    private Lecturer lecturer;
    private UserAccount user = new UserAccount("lecturer1", "pass", Role.LECTURER);
    private Course course;
    private Student student;

    @BeforeTransaction
    public void beforeTransaction() {
        lecturer = lecturerRepository.save(new Lecturer("2", "Lecturer", "lecturer@lecturer.com", user));
    }

    @AfterTransaction
    public void afterTransaction() {
        lecturerRepository.delete(lecturer);
    }

    @Before
    public void setUp() {
        student = studentRepository.save(new Student("1", "student1", "s@student.com"));
        Subject subject = subjectRepository.save(new Subject("ASE", BigDecimal.valueOf(6)));
        subject.addLecturers(lecturer);
        subjectRepository.save(subject);
        Semester semester = semesterRepository.save(new Semester("2016WS"));
        course = courseRepository.save(new Course(subject, semester));
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void getDefaultGradeForStudentAndCourseNotAuthenticated() {
        gradeService.getDefaultGradeForStudentAndCourse(student.getId(), course.getId());
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "STUDENT")
    public void getDefaultGradeForStudentAndCourseAuthenticatedAsStudent() {
        gradeService.getDefaultGradeForStudentAndCourse(student.getId(), course.getId());
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "ADMIN")
    public void getDefaultGradeForStudentAndCourseAuthenticatedAsAdmin() {
        gradeService.getDefaultGradeForStudentAndCourse(student.getId(), course.getId());
    }

    @Test
    @WithUserDetails("lecturer1")
    public void getDefaultGradeForStudentAndCourseAuthenticatedAsLecturer() {
        gradeService.getDefaultGradeForStudentAndCourse(student.getId(), course.getId());
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void saveNewGradeForStudentAndCourseNotAuthenticated() {
        gradeService.saveNewGradeForStudentAndCourse(new Grade(course, lecturer, student, BigDecimal.ONE));
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "STUDENT")
    public void saveNewGradeForStudentAndCourseAuthenticatedAsStudent() {
        gradeService.saveNewGradeForStudentAndCourse(new Grade(course, lecturer, student, BigDecimal.ONE));
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "ADMIN")
    public void saveNewGradeForStudentAndCourseAuthenticatedAsAdmin() {
        gradeService.saveNewGradeForStudentAndCourse(new Grade(course, lecturer, student, BigDecimal.ONE));
    }

    @Test
    @WithUserDetails("lecturer1")
    public void saveNewGradeForStudentAndCourseAuthenticatedAsLecturer() {
        gradeService.saveNewGradeForStudentAndCourse(new Grade(course, lecturer, student, BigDecimal.ONE));
    }


}
