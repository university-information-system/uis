package at.ac.tuwien.inso.dao_tests;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.transaction.annotation.*;

import java.math.*;
import java.util.*;

import static at.ac.tuwien.inso.utils.IterableUtils.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class GradeRepositoryTests {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private SemesterRepository semesterRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private LecturerRepository lecturerRepository;

    private List<Student> students;

    @Before
    public void setUp() throws Exception {
        students = toList(studentRepository.save(asList(
                new Student("123", "student", "student@uis.at"),
                new Student("456", "student", "student@uis.at")
        )));
    }

    @Test
    public void findAllOfStudentWithNoGrades() throws Exception {
        prepareGradeFor(students.get(0));

        List<Grade> grades = gradeRepository.findAllOfStudent(students.get(1));

        assertThat(grades, empty());
    }

    private Grade prepareGradeFor(Student student) {
        Subject subject = subjectRepository.save(new Subject("subject", BigDecimal.ONE));
        Semester semester = semesterRepository.save(new Semester(2016, SemesterType.SummerSemester));
        Course course = courseRepository.save(new Course(subject, semester));
        Lecturer lecturer = lecturerRepository.save(new Lecturer("abc", "lecturer", "lecturer@uis.at"));

        return gradeRepository.save(new Grade(course, lecturer, student, Mark.EXCELLENT));
    }

    @Test
    public void findAllOfStudentWithSomeFeedbackEntries() throws Exception {
        Grade grade = prepareGradeFor(students.get(0));

        List<Grade> grades = gradeRepository.findAllOfStudent(students.get(0));

        assertThat(grades, equalTo(singletonList(grade)));
    }
}
