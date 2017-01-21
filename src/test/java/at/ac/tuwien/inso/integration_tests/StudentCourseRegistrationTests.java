package at.ac.tuwien.inso.integration_tests;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.web.servlet.*;
import org.springframework.transaction.annotation.*;

import java.math.*;
import java.util.ArrayList;
import java.util.*;

import static java.util.Arrays.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class StudentCourseRegistrationTests extends AbstractCoursesTests {

    UserAccount user1 = new UserAccount("lecturer1", "pass", Role.LECTURER);
    UserAccount studentUser = new UserAccount("student1", "pass", Role.STUDENT);
    UserAccount student2User = new UserAccount("student2", "pass", Role.STUDENT);
    Lecturer lecturer1 = new Lecturer("l0001", "Lecturer 1", "email", user1);
    Lecturer lecturer2 = new Lecturer("l0002", "Lecturer 2", "email", new UserAccount("lecturer2", "pass", Role.LECTURER));
    Lecturer lecturer3 = new Lecturer("l0003", "Lecturer 3", "email", new UserAccount("lecturer3", "pass", Role.LECTURER));
    Student student1 = new Student("s000001", "Student1", "email", studentUser);
    Student student2 = new Student("s000002", "Student2", "email", student2User);
    Student student3 = new Student("s000003", "Student3", "email", new UserAccount("student3", "pass", Role.STUDENT));
    Semester ss2016 = new Semester(2016, SemesterType.SummerSemester);
    Semester ws2016 = new Semester(2016, SemesterType.WinterSemester);
    Subject calculus = new Subject("Calculus", new BigDecimal(3.0));
    Subject sepm = new Subject("SEPM", new BigDecimal(6.0));
    Subject ase = new Subject("ASE", new BigDecimal(6.0));
    Course sepmSS2016 = new Course(sepm, ss2016);
    Course sepmWS2016 = new Course(sepm, ws2016);
    Course aseWS2016 = new Course(ase, ws2016);
    Course calculusWS2016 = new Course(calculus, ws2016);
    List<Grade> grades = new ArrayList<>();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private SemesterRepository semesterRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private LecturerRepository lecturerRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private GradeRepository gradeRepository;


    @Before
    public void setUp() {

        lecturerRepository.save(lecturer1);
        lecturerRepository.save(lecturer2);
        lecturerRepository.save(lecturer3);

        studentRepository.save(asList(student1, student2, student3));

        semesterRepository.save(ss2016);
        semesterRepository.save(ws2016);

        subjectRepository.save(calculus);
        calculus.addLecturers(lecturer3);
        subjectRepository.save(sepm);
        sepm.addLecturers(lecturer1);
        subjectRepository.save(ase);
        ase.addLecturers(lecturer1, lecturer2);

        sepmSS2016 = courseRepository.save(sepmSS2016);
        sepmWS2016 = courseRepository.save(sepmWS2016);
        aseWS2016.setStudentLimits(10);
        aseWS2016.addStudents(student2);
        aseWS2016 = courseRepository.save(aseWS2016);
        calculusWS2016 = courseRepository.save(calculusWS2016);

        Grade grade = new Grade(aseWS2016, lecturer1, student2, Mark.EXCELLENT);
        grade = gradeRepository.save(grade);
        grades.add(grade);

        tagRepository.save(asList(
                new Tag("Computer Science"),
                new Tag("Math"),
                new Tag("Fun"),
                new Tag("Easy"),
                new Tag("Difficult")
        ));
    }

    @Test
    public void itDoesNotRegisterStudent() throws Exception {
        String expect = "Cannot register to course \"" + sepmWS2016.getSubject().getName() + "\"";
        mockMvc.perform(
                post("/student/register").with(user(studentUser))
                        .param("courseId", sepmWS2016.getId().toString())
                        .with(csrf())
        ).andExpect(
                redirectedUrl("/student/courses")
        ).andExpect(
                flash().attribute("flashMessageNotLocalized", expect)
        );
    }

    @Test
    public void itRegistersStudent() throws Exception {
        String expect = "Registered to course \"" + aseWS2016.getSubject().getName() + "\"";
        mockMvc.perform(
                post("/student/register").with(user(studentUser))
                        .param("courseId", aseWS2016.getId().toString())
                        .with(csrf())
        ).andExpect(
                redirectedUrl("/student/courses")
        ).andExpect(
                flash().attribute("flashMessageNotLocalized", expect)
        );
    }

    @Test
    public void itUnregistersStudentFromCourseAndRedirectsToMyCoursesPage() throws Exception {
        String expected = "Unregistered from course \"" + aseWS2016.getSubject().getName() + "\"";
        mockMvc.perform(
                post("/student/unregister")
                        .with(csrf())
                        .param("course", aseWS2016.getId().toString())
                        .with(user(student2User))
        ).andExpect(result ->
                assertThat(courseRepository.findOne(aseWS2016.getId()).getStudents(), empty())
        ).andExpect(
                redirectedUrl("/student/myCourses")
        ).andExpect(
                flash().attribute("flashMessageNotLocalized", expected)
        );
    }
}
