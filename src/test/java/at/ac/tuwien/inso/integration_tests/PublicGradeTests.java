package at.ac.tuwien.inso.integration_tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Grade;
import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.entity.Mark;
import at.ac.tuwien.inso.entity.Role;
import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.entity.SemesterType;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.entity.UserAccount;
import at.ac.tuwien.inso.repository.CourseRepository;
import at.ac.tuwien.inso.repository.GradeRepository;
import at.ac.tuwien.inso.repository.LecturerRepository;
import at.ac.tuwien.inso.repository.SemesterRepository;
import at.ac.tuwien.inso.repository.StudentRepository;
import at.ac.tuwien.inso.repository.SubjectRepository;

import static java.util.Arrays.asList;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class PublicGradeTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private StudentRepository studentRepository;

    private Grade grade;

    @Before
    public void setUp() {

        Subject subject = new Subject("Test", new BigDecimal(3));
        subjectRepository.save(subject);

        Semester semester = new Semester(2017, SemesterType.SummerSemester);
        semesterRepository.save(semester);

        Course course = new Course(subject, semester);
        courseRepository.save(course);

        Lecturer lecturer = new Lecturer("123", "TestLecturer", "test@lecturer.com");
        lecturerRepository.save(lecturer);

        Student student = new Student("456", "TestStudent", "test@student.com");
        studentRepository.save(student);

        grade = gradeRepository.save(new Grade(course, lecturer, student, Mark.EXCELLENT));
        grade.getId();
    }

    @Test
    public void generateGradePDFTest() throws Exception {
        grade = gradeRepository.findOne(grade.getId());
        mockMvc.perform(
                get("/public/grade?identifier=" + grade.getUrlIdentifier())
                        .with(anonymous())
                        .accept(MediaType.APPLICATION_PDF)
        ).andExpect(
                model().attribute("grade", grade)
        );
    }
}
