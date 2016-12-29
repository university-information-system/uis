package at.ac.tuwien.inso.dao_tests;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import org.hamcrest.*;
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
public class CoursesRepositoryTests {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    private List<Student> students;

    private Map<String, Tag> tags = new HashMap<String, Tag>() {
        {
            put("Tag1", new Tag("Tag1"));
            put("Tag2", new Tag("Tag2"));
            put("Tag3", new Tag("Tag3"));
        }
    };

    private Map<String, Subject> subjects = new HashMap<String, Subject>() {
        {
            put("Subject1", new Subject("Subject1", new BigDecimal(3.0)));
            put("Subject2", new Subject("Subject2", new BigDecimal(6.0)));
            put("Subject3", new Subject("Subject3", new BigDecimal(6.0)));
            put("Subject4", new Subject("Subject3", new BigDecimal(6.0)));
            put("Subject5", new Subject("Subject3", new BigDecimal(6.0)));
        }
    };

    private Map<String, Semester> semesters = new HashMap<String, Semester>() {
        {
            put("WS2015", new Semester("WS2015"));
            put("WS2016", new Semester("WS2016"));
        }
    };

    private Map<String, Course> courses = new HashMap<String, Course>() {
        {
            put("Course1", new Course(subjects.get("Subject1"), semesters.get("WS2016")));
            put("Course2", new Course(subjects.get("Subject2"), semesters.get("WS2016")));
            put("Course3", new Course(subjects.get("Subject3"), semesters.get("WS2016")));

            put("Course4", new Course(subjects.get("Subject4"), semesters.get("WS2015")));
            put("Course5", new Course(subjects.get("Subject5"), semesters.get("WS2015")));
        }
    };

    private void addTags() {
        courses.get("Course1").addTags(tags.get("Tag1"), tags.get("Tag2"), tags.get("Tag3"));
        courses.get("Course2").addTags(tags.get("Tag1"), tags.get("Tag2"));
        courses.get("Course3").addTags(tags.get("Tag1"));
    }

    @Before
    public void setUp() throws Exception {
        students = toList(studentRepository.save(asList(
                new Student("123", "student", "student@uis.at"),
                new Student("456", "student", "student@uis.at")
        )));

        tagRepository.save(tags.values());
        subjectRepository.save(subjects.values());
        semesterRepository.save(semesters.values());
        courseRepository.save(courses.values());

        addTags();
    }

    @Test
    public void itReturnsCoursesForCurrentSemester() throws Exception {
        List<Course> actual = courseRepository.findAllByCurrentSemester();

        assertThat(actual, CoreMatchers.hasItems(courses.get("Course1"), courses.get("Course2"), courses.get("Course3")));
    }

    @Test
    public void findAllForStudentWithEmptyCourses() throws Exception {
        courses.values().forEach(it -> it.addStudents(students.get(0)));

        List<Course> courses = courseRepository.findAllForStudent(students.get(1));

        assertThat(courses, empty());
    }

    @Test
    public void findAllForStudentWithCourseRegistrations() throws Exception {
        Course course = courses.get("Course1");
        Student student = students.get(0);
        course.addStudents(student);

        List<Course> courses = courseRepository.findAllForStudent(student);

        assertThat(courses, equalTo(singletonList(course)));
    }

    @Test
    public void testExistsCourseRegistrationForStudentRegisteredToCourse() throws Exception {
        Course course = courses.get("Course1");
        Student student = students.get(0);
        course.addStudents(student);

        assertTrue(courseRepository.existsCourseRegistration(student, course));
    }

    @Test
    public void testExistsCourseRegistrationForStudentNotRegisteredToCourse() throws Exception {
        Course course = courses.get("Course1");
        Student student = students.get(0);
        course.addStudents(student);

        assertFalse(courseRepository.existsCourseRegistration(student, courses.get("Course2")));
    }
}
