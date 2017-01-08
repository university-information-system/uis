package at.ac.tuwien.inso.method_security_tests;

import at.ac.tuwien.inso.controller.lecturer.GradeAuthorizationDTO;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.exception.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.security.access.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.test.context.support.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.context.transaction.*;
import org.springframework.transaction.annotation.*;

import java.math.*;


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
        gradeService.getDefaultGradeAuthorizationDTOForStudentAndCourse(student.getId(), course.getId());
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "STUDENT")
    public void getDefaultGradeForStudentAndCourseAuthenticatedAsStudent() {
        gradeService.getDefaultGradeAuthorizationDTOForStudentAndCourse(student.getId(), course.getId());
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "ADMIN")
    public void getDefaultGradeForStudentAndCourseAuthenticatedAsAdmin() {
        gradeService.getDefaultGradeAuthorizationDTOForStudentAndCourse(student.getId(), course.getId());
    }

    @Test(expected = ValidationException.class)
    @WithUserDetails("lecturer1")
    public void getDefaultGradeForStudentAndCourseAuthenticatedAsLecturer() {
        gradeService.getDefaultGradeAuthorizationDTOForStudentAndCourse(student.getId(), course.getId());
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void saveNewGradeForStudentAndCourseNotAuthenticated() {
        gradeService.saveNewGradeForStudentAndCourse(new GradeAuthorizationDTO(new Grade(course, lecturer, student, Mark.EXCELLENT)));
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "STUDENT")
    public void saveNewGradeForStudentAndCourseAuthenticatedAsStudent() {
        gradeService.saveNewGradeForStudentAndCourse(new GradeAuthorizationDTO(new Grade(course, lecturer, student, Mark.EXCELLENT)));
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "ADMIN")
    public void saveNewGradeForStudentAndCourseAuthenticatedAsAdmin() {
        gradeService.saveNewGradeForStudentAndCourse(new GradeAuthorizationDTO(new Grade(course, lecturer, student, Mark.EXCELLENT)));
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void getGradesForLoggedInStudentNotAuthenticated() {
        gradeService.getGradesForLoggedInStudent();
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "ADMIN")
    public void getGradesForLoggedInStudentAsAdmin() {
        gradeService.getGradesForLoggedInStudent();
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "LECTURER")
    public void getGradesForLoggedInStudentAsLecturer() {
        gradeService.getGradesForLoggedInStudent();
    }

    @Test(expected = UsernameNotFoundException.class)
    @WithMockUser(roles = "STUDENT")
    public void getGradesForLoggedInStudentAsStudent() {
        gradeService.getGradesForLoggedInStudent();
    }

    @Test
    public void getForValidationTestNotAuthenticated() {
        gradeService.getForValidation("1");
    }


}
