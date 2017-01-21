package at.ac.tuwien.inso.integration_tests;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AbstractCoursesTests {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected CourseRepository courseRepository;

    @Autowired
    protected TagRepository tagRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private StudentRepository studentRepository;

    protected UserAccount user1 = new UserAccount("lecturer1", "pass", Role.LECTURER);
    protected Lecturer lecturer1 = new Lecturer("l0001", "Lecturer 1", "email", user1);
    protected Lecturer lecturer2 = new Lecturer("l0002", "Lecturer 2", "email", new UserAccount("lecturer2", "pass", Role.LECTURER));
    protected Lecturer lecturer3 = new Lecturer("l0003", "Lecturer 3", "email", new UserAccount("lecturer3", "pass", Role.LECTURER));
    protected Student student = new Student("s1234", "Student", "student@uis.at", new UserAccount("student", "pass", Role.STUDENT));
    protected Semester ss2016 = new Semester(2016, SemesterType.SummerSemester);
    protected Semester ws2016 = new Semester(2016, SemesterType.WinterSemester);
    protected Subject calculus = new Subject("Calculus", new BigDecimal(3.0));
    protected Subject sepm = new Subject("SEPM", new BigDecimal(6.0));
    protected Subject ase = new Subject("ASE", new BigDecimal(6.0));
    protected Course sepmSS2016 = new Course(sepm, ss2016);
    protected Course sepmWS2016 = new Course(sepm, ws2016);
    protected Course aseWS2016 = new Course(ase, ws2016);
    protected Course calculusWS2016 = new Course(calculus, ws2016);

    protected List<Course> expectedCourses;
    protected List<Course> expectedCoursesForLecturer1;
    protected List<Course> expectedCoursesForLecturer2;
    protected List<Course> expectedCoursesForLecturer3;

    @Before
    public void setUp() {
        student = studentRepository.save(student);

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
        ase.addLecturers(lecturer1, lecturer2);

        calculusWS2016.addStudents(student);

        sepmSS2016 = courseRepository.save(sepmSS2016);
        sepmWS2016 = courseRepository.save(sepmWS2016);
        aseWS2016 = courseRepository.save(aseWS2016);
        calculusWS2016 = courseRepository.save(calculusWS2016);

        expectedCourses = Arrays.asList(sepmWS2016, aseWS2016, calculusWS2016);
        expectedCoursesForLecturer1 = Arrays.asList(sepmWS2016, aseWS2016);
        expectedCoursesForLecturer3 = Arrays.asList(aseWS2016);
        expectedCoursesForLecturer2 = Arrays.asList(calculusWS2016);

        tagRepository.save(asList(
                new Tag("Computer Science"),
                new Tag("Math"),
                new Tag("Fun"),
                new Tag("Easy"),
                new Tag("Difficult")
        ));
    }


}
