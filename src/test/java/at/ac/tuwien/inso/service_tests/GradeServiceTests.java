package at.ac.tuwien.inso.service_tests;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.exception.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.*;
import at.ac.tuwien.inso.service.impl.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.runners.*;
import org.springframework.security.test.context.support.*;
import org.springframework.test.context.*;

import java.math.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("test")
public class GradeServiceTests {

    private static final Long VALID_STUDENT_ID = 1L;
    private static final Long VALID_UNREGISTERED_STUDENT_ID = 2L;
    private static final Long INVALID_STUDENT_ID = 1337L;
    private static final Long VALID_COURSE_ID = 3L;
    private static final Long INVALID_COURSE_ID = 337L;
    private GradeService gradeService;
    @Mock
    private LecturerService lecturerService;
    @Mock
    private StudentService studentService;
    @Mock
    private CourseService courseService;
    @Mock
    private UserAccountService userAccountService;
    @Mock
    private GradeRepository gradeRepository;
    private Course course = new Course(new Subject("ASE", BigDecimal.ONE), new Semester("WS16"));
    private Student student = new Student("Student", "Student", "Student@student.com");
    private Student unregisteredStudent = new Student("Student2", "Student2", "not@registered.com");

    private Lecturer lecturer = new Lecturer("Lecturer", "Lecturer", "lecturer@lecturer.com");

    private Grade validGrade = new Grade(course, lecturer, student, Mark.SATISFACTORY);

    @Before
    public void setUp() {

        course.getStudents().add(student);

        MockitoAnnotations.initMocks(this);
        when(courseService.findOne(VALID_COURSE_ID)).thenReturn(course);
        when(courseService.findOne(INVALID_COURSE_ID)).thenReturn(null);
        when(studentService.findOne(VALID_STUDENT_ID)).thenReturn(student);
        when(studentService.findOne(VALID_UNREGISTERED_STUDENT_ID)).thenReturn(unregisteredStudent);
        when(studentService.findOne(INVALID_STUDENT_ID)).thenReturn(null);
        when(lecturerService.getLoggedInLecturer()).thenReturn(lecturer);
        when(gradeRepository.save(validGrade)).thenReturn(validGrade);


        gradeService = new GradeServiceImpl(
                gradeRepository, studentService, courseService, lecturerService, userAccountService
        );
    }

    @Test
    @WithMockUser(roles = "Lecturer")
    public void getDefaultGradeForStudentAndCourseTest() {
        Grade grade = gradeService.getDefaultGradeForStudentAndCourse(VALID_STUDENT_ID, VALID_COURSE_ID);
        assertEquals(grade.getCourse(), course);
        assertEquals(grade.getStudent(), student);
        assertEquals(grade.getLecturer(), lecturer);
        assertEquals(grade.getMark(), Mark.FAILED);
        assertNull(grade.getId());
    }

    @Test(expected = BusinessObjectNotFoundException.class)
    @WithMockUser(roles = "Lecturer")
    public void getDefaultGradeForInvalidStudentAndCourseTest() {
        gradeService.getDefaultGradeForStudentAndCourse(INVALID_STUDENT_ID, VALID_COURSE_ID);
    }


    @Test(expected = BusinessObjectNotFoundException.class)
    @WithMockUser(roles = "Lecturer")
    public void getDefaultGradeForStudentAndInvalidCourseTest() {
        gradeService.getDefaultGradeForStudentAndCourse(VALID_STUDENT_ID, INVALID_COURSE_ID);
    }

    @Test(expected = BusinessObjectNotFoundException.class)
    @WithMockUser(roles = "Lecturer")
    public void getDefaultGradeForInvalidStudentAndInvalidCourseTest() {
        gradeService.getDefaultGradeForStudentAndCourse(INVALID_STUDENT_ID, INVALID_COURSE_ID);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(roles = "Lecturer")
    public void getDefaultGradeForUnregisteredStudentAndCourseTest() {
        gradeService.getDefaultGradeForStudentAndCourse(VALID_UNREGISTERED_STUDENT_ID, VALID_COURSE_ID);
    }

    @Test
    @WithMockUser(roles = "Lecturer")
    public void saveNewGradeForStudentAndCourseTest() {
        Grade result = gradeService.saveNewGradeForStudentAndCourse(validGrade);
        assertEquals(validGrade.getLecturer(), result.getLecturer());
        assertEquals(validGrade.getStudent(), result.getStudent());
        assertEquals(validGrade.getCourse(), result.getCourse());
        assertEquals(validGrade.getMark(), result.getMark());
    }
}
