package at.ac.tuwien.inso;


import org.junit.Before;
import org.junit.Test;
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

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.entity.Role;
import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.entity.UserAccount;
import at.ac.tuwien.inso.entity.UserProfile;
import at.ac.tuwien.inso.repository.CourseRepository;
import at.ac.tuwien.inso.repository.LecturerRepository;
import at.ac.tuwien.inso.repository.RoleRepository;
import at.ac.tuwien.inso.repository.SemesterRepository;
import at.ac.tuwien.inso.repository.SubjectRepository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CoursesTests {

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
    private RoleRepository roleRepository;


    private List<Course> expectedCourses;

    Role lecturerRole = new Role("ROLE_LECTURER");

    Lecturer lecturer1 = new Lecturer(new UserProfile("Lecturer 1", "email", new UserAccount("lecturer1", "pass", lecturerRole)));
    Lecturer lecturer2 = new Lecturer(new UserProfile("Lecturer 2", "email", new UserAccount("lecturer2", "pass", lecturerRole)));
    Lecturer lecturer3 = new Lecturer(new UserProfile("Lecturer 3", "email", new UserAccount("lecturer3", "pass", lecturerRole)));

    Semester ss2016 = new Semester("SS2016");
    Semester ws2016 = new Semester("WS2016");

    Subject calculus = new Subject("Calculus", new BigDecimal(3.0));
    Subject sepm = new Subject("SEPM", new BigDecimal(6.0));
    Subject ase = new Subject("ASE", new BigDecimal(6.0));

    Course sepmSS2016 = new Course(sepm, ss2016);
    Course sepmWS2016 = new Course(sepm, ws2016);
    Course aseWS2016 = new Course(ase, ws2016);
    Course calculusWS2016 = new Course(calculus, ws2016);


    @Before
    public void setUp() {

        roleRepository.save(lecturerRole);

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

    }

    @Test
    @Transactional
    public void itListsAllCoursesForCurrentSemester() throws Exception {
        mockMvc.perform(
                get("/student/courses").with(user("student").roles("STUDENT"))
        ).andExpect(
                model().attribute("allCourses", expectedCourses)
        );
    }

    @Test
    @Transactional
    public void itListsAllCoursesForCurrentSemesterWithNameFilterNoString() throws Exception {
        mockMvc.perform(
                get("/student/courses?search=").with(user("student").roles("STUDENT"))
        ).andExpect(
                model().attribute("allCourses", expectedCourses)
        );
    }

    @Test
    @Transactional
    public void itListsAllCoursesForCurrentSemesterWithNameFilter() throws Exception {
        mockMvc.perform(
                get("/student/courses?search=sep").with(user("student").roles("STUDENT"))
        ).andExpect(
                model().attribute("allCourses", Arrays.asList(sepmWS2016))
        );
    }

}
