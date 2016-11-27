package at.ac.tuwien.inso.service_tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Grade;
import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.exception.BusinessObjectNotFoundException;
import at.ac.tuwien.inso.exception.ValidationException;
import at.ac.tuwien.inso.repository.GradeRepository;
import at.ac.tuwien.inso.service.CourseService;
import at.ac.tuwien.inso.service.GradeService;
import at.ac.tuwien.inso.service.LecturerService;
import at.ac.tuwien.inso.service.StudentService;
import at.ac.tuwien.inso.service.impl.GradeServiceImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("test")
public class GradeServiceTests {

    private GradeService gradeService;

    @Mock
    private LecturerService lecturerService;

    @Mock
    private StudentService studentService;

    @Mock
    private CourseService courseService;

    @Mock
    private GradeRepository gradeRepository;

    private static final Long VALID_STUDENT_ID = 1L;
    private static final Long VALID_UNREGISTERED_STUDENT_ID = 2L;
    private static final Long INVALID_STUDENT_ID = 1337L;

    private static final Long VALID_COURSE_ID = 3L;
    private static final Long INVALID_COURSE_ID = 337L;

    private Course course = new Course(new Subject("ASE", BigDecimal.ONE), new Semester("WS16"));
    private Student student = new Student("Student", "Student", "Student@student.com");
    private Student unregisteredStudent = new Student("Student2", "Student2", "not@registered.com");

    private Lecturer lecturer = new Lecturer("Lecturer", "Lecturer", "lecturer@lecturer.com");

    private Grade validGrade = new Grade(course, lecturer, student, BigDecimal.valueOf(3));
    private Grade invalidGradeHigh = new Grade(course, lecturer, student, BigDecimal.valueOf(7));
    private Grade invalidGradeLow = new Grade(course, lecturer, student, BigDecimal.valueOf(0));


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

        gradeService = new GradeServiceImpl(gradeRepository, studentService, courseService, lecturerService);
    }

    @Test
    @WithMockUser(roles = "Lecturer")
    public void getDefaultGradeForStudentAndCourseTest() {
        Grade grade = gradeService.getDefaultGradeForStudentAndCourse(VALID_STUDENT_ID, VALID_COURSE_ID);
        assertEquals(grade.getCourse(), course);
        assertEquals(grade.getStudent(), student);
        assertEquals(grade.getLecturer(), lecturer);
        assertEquals(grade.getMark(), BigDecimal.valueOf(5));
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

    @Test(expected = ValidationException.class)
    @WithMockUser(roles = "Lecturer")
    public void saveNewGradeForStudentAndCourseLowMarkTest() {
        gradeService.saveNewGradeForStudentAndCourse(invalidGradeLow);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(roles = "Lecturer")
    public void saveNewGradeForStudentAndCourseHighMarkTest() {
        gradeService.saveNewGradeForStudentAndCourse(invalidGradeHigh);
    }

}
