package at.ac.tuwien.inso.integration_tests;

import static java.util.Arrays.asList;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Grade;
import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.entity.Mark;
import at.ac.tuwien.inso.entity.Role;
import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.entity.SemesterType;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.entity.Tag;
import at.ac.tuwien.inso.entity.UserAccount;
import at.ac.tuwien.inso.repository.CourseRepository;
import at.ac.tuwien.inso.repository.GradeRepository;
import at.ac.tuwien.inso.repository.LecturerRepository;
import at.ac.tuwien.inso.repository.SemesterRepository;
import at.ac.tuwien.inso.repository.StudentRepository;
import at.ac.tuwien.inso.repository.SubjectRepository;
import at.ac.tuwien.inso.repository.TagRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class StudentCourseRegistrationTests extends AbstractCoursesTests {

    UserAccount user1 = new UserAccount("lecturer1", "pass", Role.LECTURER);
    UserAccount studentUser = new UserAccount("student1", "pass", Role.STUDENT);
    UserAccount student2User = new UserAccount("student2", "pass", Role.STUDENT);
    Lecturer lecturer1 = new Lecturer("l0001", "Lecturer 1", "email1@uis.at", user1);
    Lecturer lecturer2 = new Lecturer("l0002", "Lecturer 2", "email2@uis.at", new UserAccount("lecturer2", "pass", Role.LECTURER));
    Lecturer lecturer3 = new Lecturer("l0003", "Lecturer 3", "email3@uis.at", new UserAccount("lecturer3", "pass", Role.LECTURER));
    Student student1 = new Student("s000001", "student1", "email11@uis.at", studentUser);
    Student student2 = new Student("s000002", "student2", "email12@uis.at", student2User);
    Student student3 = new Student("s000003", "student3", "email13@uis.at", new UserAccount("student3", "pass", Role.STUDENT));
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

        mockMvc.perform(
                post("/student/register/" + sepmWS2016.getId()).with(user(studentUser))
                        .with(csrf())
        ).andExpect(
                redirectedUrl("/student/courses")
        );

        assertFalse(aseWS2016.getStudents().contains(student1));
    }

    @Test
    public void itRegistersStudent() throws Exception {

        mockMvc.perform(
                post("/student/register/" + aseWS2016.getId()).with(user(studentUser))
                        .with(csrf())
        ).andExpect(
                redirectedUrl("/student/courses")
        );

        assertTrue(aseWS2016.getStudents().contains(student1));
    }

    @Test
    public void itUnregistersStudentFromCourseAndRedirectsToMyCoursesPage() throws Exception {

        mockMvc.perform(
                post("/student/unregister")
                        .with(csrf())
                        .param("course", aseWS2016.getId().toString())
                        //.with(user(student2User))
                        .with(user("student2").roles(Role.STUDENT.name()))
        ).andExpect(result ->
                assertThat(courseRepository.findOne(aseWS2016.getId()).getStudents(), empty())
        ).andExpect(
                redirectedUrl("/student/myCourses")
        );

        assertFalse(aseWS2016.getStudents().contains(student2));
    }
}
