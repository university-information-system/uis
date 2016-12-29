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
public class FeedbackRepositoryTests {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private SemesterRepository semesterRepository;
    @Autowired
    private SubjectRepository subjectRepository;

    private List<Student> students;

    private Course course;

    @Before
    public void setUp() throws Exception {
        prepareStudents();
        prepareCourse();
    }

    private void prepareStudents() {
        students = toList(studentRepository.save(asList(
                new Student("123", "student", "student@uis.at"),
                new Student("456", "student", "student@uis.at")
        )));
    }

    private void prepareCourse() {
        Subject subject = subjectRepository.save(new Subject("subject", BigDecimal.ONE));
        Semester semester = semesterRepository.save(new Semester("current"));

        course = courseRepository.save(new Course(subject, semester));
    }

    @Test
    public void findAllOfStudentWithEmptyFeedbackEntries() throws Exception {
        prepareFeedbackFor(students.get(0));

        List<Feedback> feedbackEntries = feedbackRepository.findAllOfStudent(students.get(1));

        assertThat(feedbackEntries, empty());
    }

    private Feedback prepareFeedbackFor(Student student) {
        return feedbackRepository.save(new Feedback(student, course));
    }

    @Test
    public void findAllOfStudentWithSomeFeedbackEntries() throws Exception {
        Feedback feedback = prepareFeedbackFor(students.get(0));

        List<Feedback> feedbackEntries = feedbackRepository.findAllOfStudent(students.get(0));

        assertThat(feedbackEntries, equalTo(singletonList(feedback)));
    }

    @Test
    public void testExistsForExistentFeedback() throws Exception {
        Feedback feedback = prepareFeedbackFor(students.get(0));

        assertTrue(feedbackRepository.exists(feedback));
    }

    @Test
    public void testExistsForNonExistentFeedback() throws Exception {
        Feedback feedback = new Feedback(students.get(0), course);

        assertFalse(feedbackRepository.exists(feedback));
    }
}
