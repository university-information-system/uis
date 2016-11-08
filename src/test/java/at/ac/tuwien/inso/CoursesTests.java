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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
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


    List<Course> expectedCourses;


    @Before
    public void setUp() {

        // create roles
        Role adminRole = roleRepository.save(new Role("ROLE_ADMIN"));
        Role lecturerRole = roleRepository.save(new Role("ROLE_LECTURER"));
        Role studentRole = roleRepository.save(new Role("ROLE_STUDENT"));

        //lecturers
        Lecturer lecturer1 = lecturerRepository.save(new Lecturer(new UserProfile("Lecturer 1", "email", new UserAccount("lecturer1", "pass",lecturerRole))));
        Lecturer lecturer2 = lecturerRepository.save(new Lecturer(new UserProfile("Lecturer 2", "email", new UserAccount("lecturer2", "pass", lecturerRole))));
        Lecturer lecturer3 = lecturerRepository.save(new Lecturer(new UserProfile("Lecturer 3", "email", new UserAccount("lecturer3", "pass", lecturerRole))));

        //create semesters
        Semester ss2016 = semesterRepository.save(new Semester("SS2016"));
        Semester ws2016 = semesterRepository.save(new Semester("WS2016"));

        //subjects
        Subject calculus = subjectRepository.save(new Subject("Calculus", new BigDecimal(3.0)));
        calculus.addLecturers(lecturer3);
        Subject sepm = subjectRepository.save(new Subject("SEPM", new BigDecimal(6.0)));
        sepm.addLecturers(lecturer1);
        Subject ase = subjectRepository.save(new Subject("ASE", new BigDecimal(6.0)));
        ase.addRequiredSubjects(sepm);
        ase.addLecturers(lecturer1, lecturer2);

        //courses
        Course sepmSS2016 = courseRepository.save(new Course(sepm,ss2016));
        Course sepmWS2016 = courseRepository.save(new Course(sepm,ws2016));
//        sepmWS2016.addStudents(student4);
        Course aseWS2016 = courseRepository.save(new Course(ase,ws2016));
//        aseWS2016.addStudents(student1, student2, student3, student4);
        Course calculusWS2016 = courseRepository.save(new Course(calculus, ws2016));
//        calculusWS2016.addStudents(student1, student4);

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

}
