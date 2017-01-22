package at.ac.tuwien.inso.dao_tests;

import static at.ac.tuwien.inso.utils.IterableUtils.toList;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Grade;
import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.entity.Mark;
import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.entity.SemesterType;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.entity.Tag;
import at.ac.tuwien.inso.repository.CourseRepository;
import at.ac.tuwien.inso.repository.GradeRepository;
import at.ac.tuwien.inso.repository.LecturerRepository;
import at.ac.tuwien.inso.repository.SemesterRepository;
import at.ac.tuwien.inso.repository.StudentRepository;
import at.ac.tuwien.inso.repository.SubjectRepository;
import at.ac.tuwien.inso.repository.TagRepository;

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

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private LecturerRepository lecturerRepository;

    private List<Student> students;

    private Lecturer lecturer;

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
            put("WS2015", new Semester(2015, SemesterType.WinterSemester));
            put("WS2016", new Semester(2016, SemesterType.WinterSemester));
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
        lecturer = lecturerRepository.save(new Lecturer("l123", "lecturer", "lecturer@uis.at"));

        tagRepository.save(tags.values());
        subjectRepository.save(subjects.values());
        semesterRepository.save(semesters.values());
        courseRepository.save(courses.values());

        addTags();
    }

    @Test
    public void verifyRecommendableCoursesForStudent() throws Exception {
        List<Course> actual = courseRepository.findAllRecommendableForStudent(students.get(0));

        assertThat(actual, CoreMatchers.hasItems(courses.get("Course1"), courses.get("Course2"), courses.get("Course3")));
    }

    @Test
    public void verifyRecommendableCoursesForStudentWithRegistrations() throws Exception {
        Student student = students.get(0);
        courses.get("Course1").addStudents(student);

        List<Course> actual = courseRepository.findAllRecommendableForStudent(student);

        assertThat(actual, not(hasItem(courses.get("Course1"))));
    }

    @Test
    public void verifyRecommendableCoursesForStudentWithNegativeGrade() throws Exception {
        addGradeForCourseInOlderSemester(Mark.FAILED);

        List<Course> actual = courseRepository.findAllRecommendableForStudent(students.get(0));

        assertThat(actual, hasItem(courses.get("Course1")));
    }

    private void addGradeForCourseInOlderSemester(Mark mark) {
        Course olderCourse = courseRepository.save(new Course(courses.get("Course1").getSubject(), semesters.get("WS2015")));

        gradeRepository.save(new Grade(olderCourse, lecturer, students.get(0), mark));
    }

    @Test
    public void verifyRecommendableCoursesForStudentWithPositiveGrade() throws Exception {
        addGradeForCourseInOlderSemester(Mark.SATISFACTORY);

        List<Course> actual = courseRepository.findAllRecommendableForStudent(students.get(0));

        assertThat(actual, not(hasItem(courses.get("Course1"))));
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
