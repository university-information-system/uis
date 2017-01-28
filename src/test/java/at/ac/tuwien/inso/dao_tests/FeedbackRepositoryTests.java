package at.ac.tuwien.inso.dao_tests;

import static at.ac.tuwien.inso.utils.IterableUtils.toList;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Feedback;
import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.entity.SemesterType;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.repository.CourseRepository;
import at.ac.tuwien.inso.repository.FeedbackRepository;
import at.ac.tuwien.inso.repository.SemesterRepository;
import at.ac.tuwien.inso.repository.StudentRepository;
import at.ac.tuwien.inso.repository.SubjectRepository;

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
                new Student("123", "student", "student123@uis.at"),
                new Student("456", "student", "student456@uis.at")
        )));
    }

    private void prepareCourse() {
        Subject subject = subjectRepository.save(new Subject("subject", BigDecimal.ONE));
        Semester semester = semesterRepository.save(new Semester(2016, SemesterType.SummerSemester));

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
