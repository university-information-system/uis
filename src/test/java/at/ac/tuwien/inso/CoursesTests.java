package at.ac.tuwien.inso;


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
import java.util.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class CoursesTests {

    UserAccount user1 = new UserAccount("lecturer1", "pass", Role.LECTURER);
    Lecturer lecturer1 = new Lecturer("Lecturer 1", "email", user1);
    Lecturer lecturer2 = new Lecturer("Lecturer 2", "email", new UserAccount("lecturer2", "pass", Role.LECTURER));
    Lecturer lecturer3 = new Lecturer("Lecturer 3", "email", new UserAccount("lecturer3", "pass", Role.LECTURER));
    Semester ss2016 = new Semester("SS2016");
    Semester ws2016 = new Semester("WS2016");
    Subject calculus = new Subject("Calculus", new BigDecimal(3.0));
    Subject sepm = new Subject("SEPM", new BigDecimal(6.0));
    Subject ase = new Subject("ASE", new BigDecimal(6.0));
    Course sepmSS2016 = new Course(sepm, ss2016);
    Course sepmWS2016 = new Course(sepm, ws2016);
    Course aseWS2016 = new Course(ase, ws2016);
    Course calculusWS2016 = new Course(calculus, ws2016);
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

    private List<Course> expectedCourses;
    private List<Course> expectedCoursesForLecturer1;
    private List<Course> expectedCoursesForLecturer2;
    private List<Course> expectedCoursesForLecturer3;

    @Before
    public void setUp() {

        lecturerRepository.save(lecturer1);
        lecturerRepository.save(lecturer2);
        lecturerRepository.save(lecturer3);

        semesterRepository.save(ss2016);
        semesterRepository.save(ws2016);

        subjectRepository.save(calculus);
        calculus.addLecturers(lecturer3);
        subjectRepository.save(sepm);
        sepm.addLecturers(lecturer1);
        subjectRepository.save(ase);
        ase.addRequiredSubjects(sepm);
        ase.addLecturers(lecturer1, lecturer2);

        sepmSS2016 = courseRepository.save(sepmSS2016);
        sepmWS2016 = courseRepository.save(sepmWS2016);
        aseWS2016 = courseRepository.save(aseWS2016);
        calculusWS2016 = courseRepository.save(calculusWS2016);

        expectedCourses = Arrays.asList(sepmWS2016, aseWS2016, calculusWS2016);
        expectedCoursesForLecturer1 = Arrays.asList(sepmWS2016, aseWS2016);
        expectedCoursesForLecturer3 = Arrays.asList(aseWS2016);
        expectedCoursesForLecturer2 = Arrays.asList(calculusWS2016);
    }

    @Test
    public void itListsAllCoursesForCurrentSemester() throws Exception {
        mockMvc.perform(
                get("/student/courses").with(user("student").roles("STUDENT"))
        ).andExpect(
                model().attribute("allCourses", expectedCourses)
        );
    }

    @Test
    public void itListsAllCoursesForCurrentSemesterWithNameFilterNoString() throws Exception {
        mockMvc.perform(
                get("/student/courses?search=").with(user("student").roles("STUDENT"))
        ).andExpect(
                model().attribute("allCourses", expectedCourses)
        );
    }

    @Test
    public void itListsAllCoursesForCurrentSemesterWithNameFilter() throws Exception {
        mockMvc.perform(
                get("/student/courses?search=sep").with(user("student").roles("STUDENT"))
        ).andExpect(
                model().attribute("allCourses", Arrays.asList(sepmWS2016))
        );
    }

    @Test
    public void itListsAllCoursesForCurrentSemesterAndLecturer() throws Exception {

        mockMvc.perform(
                get("/lecturer/courses").with(user(user1))
        ).andExpect(
                model().attribute("allCourses", expectedCoursesForLecturer1)
        );
    }

    @Test
    @Ignore
    public void itCreatesCourseForCurrentSemesterSubjectAndLecturer() throws Exception {
        //TODO: add test
    }

}
