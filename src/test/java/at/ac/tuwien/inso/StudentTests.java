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

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.entity.Role;
import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.entity.Tag;
import at.ac.tuwien.inso.entity.UserAccount;
import at.ac.tuwien.inso.repository.CourseRepository;
import at.ac.tuwien.inso.repository.LecturerRepository;
import at.ac.tuwien.inso.repository.SemesterRepository;
import at.ac.tuwien.inso.repository.StudentRepository;
import at.ac.tuwien.inso.repository.SubjectRepository;
import at.ac.tuwien.inso.repository.TagRepository;

import static java.util.Arrays.asList;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class StudentTests {

    UserAccount user1 = new UserAccount("lecturer1", "pass", Role.LECTURER);
    UserAccount studentUser = new UserAccount("student1", "pass", Role.STUDENT);
    Lecturer lecturer1 = new Lecturer("Lecturer 1", "email", user1);
    Lecturer lecturer2 = new Lecturer("Lecturer 2", "email", new UserAccount("lecturer2", "pass", Role.LECTURER));
    Lecturer lecturer3 = new Lecturer("Lecturer 3", "email", new UserAccount("lecturer3", "pass", Role.LECTURER));
    Student student1 = new Student("Student1", "email", studentUser);
    Student student2 = new Student("Student2", "email", new UserAccount("student2", "pass", Role.STUDENT));
    Student student3 = new Student("Student3", "email", new UserAccount("student3", "pass", Role.STUDENT));
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
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private StudentRepository studentRepository;


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
        ase.addRequiredSubjects(sepm);
        ase.addLecturers(lecturer1, lecturer2);

        sepmSS2016 = courseRepository.save(sepmSS2016);
        sepmWS2016 = courseRepository.save(sepmWS2016);
        aseWS2016.setStudentLimits(10);
        aseWS2016 = courseRepository.save(aseWS2016);
        calculusWS2016 = courseRepository.save(calculusWS2016);


        tagRepository.save(asList(
                new Tag("Computer Science"),
                new Tag("Math"),
                new Tag("Fun"),
                new Tag("Easy"),
                new Tag("Difficult")
        ));
    }

    @Test
    public void itDoesNotRegisterStudent() throws Exception{
        mockMvc.perform(
                get("/student/register").with(user(studentUser))
                        .param("courseId", sepmWS2016.getId().toString())
        ).andExpect(
                redirectedUrl("/student/courses")
        ).andExpect(
                flash().attribute("notRegisteredForCourse", sepmWS2016.getSubject().getName())
        );
    }

    @Test
    public void itRegistersStudent() throws Exception{
        mockMvc.perform(
                get("/student/register").with(user(studentUser))
                        .param("courseId", aseWS2016.getId().toString())
        ).andExpect(
                redirectedUrl("/student/courses")
        ).andExpect(
                flash().attribute("registeredForCourse", aseWS2016.getSubject().getName())
        );
    }

}
