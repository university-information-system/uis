package at.ac.tuwien.inso.dao_tests;

import static java.util.Arrays.asList;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Role;
import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.entity.SemesterType;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.entity.Tag;
import at.ac.tuwien.inso.entity.UserAccount;
import at.ac.tuwien.inso.repository.CourseRepository;
import at.ac.tuwien.inso.repository.SemesterRepository;
import at.ac.tuwien.inso.repository.StudentRepository;
import at.ac.tuwien.inso.repository.SubjectRepository;
import at.ac.tuwien.inso.repository.TagRepository;
import at.ac.tuwien.inso.repository.utils.TagFrequency;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class StudentRepositoryTests {

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
        }
    };

    private Map<String, Semester> semesters = new HashMap<String, Semester>() {
        {
            put("WS2016", new Semester(2016, SemesterType.WinterSemester));
        }
    };

    private Map<String, Course> courses = new HashMap<String, Course>() {
        {
            put("Course1", new Course(subjects.get("Subject1"), semesters.get("WS2016")));
            put("Course2", new Course(subjects.get("Subject2"), semesters.get("WS2016")));
            put("Course3", new Course(subjects.get("Subject3"), semesters.get("WS2016")));
        }
    };

    private Map<String, Student> students = new HashMap<String, Student>() {
        {
            put("Student1", new Student("s1127157", "Emma Dowd", "emma.dowd@gmail.com", new UserAccount("student", "pass", Role.STUDENT)));
            put("Student2", new Student("s1123960", "Caroline Black", "caroline.black@uis.at", null));
        }
    };

    private void addTags() {
        courses.get("Course1").addTags(tags.get("Tag1"), tags.get("Tag2"), tags.get("Tag3"));
        courses.get("Course2").addTags(tags.get("Tag1"), tags.get("Tag2"));
        courses.get("Course3").addTags(tags.get("Tag1"));
    }

    private void addCoursesToStudents() {
        asList(courses.get("Course1"), courses.get("Course2"), courses.get("Course3"))
                .forEach(it -> it.addStudents(students.get("Student2")));
    }

    @Before
    public void setUp() throws Exception {
        tagRepository.save(tags.values());
        subjectRepository.save(subjects.values());
        semesterRepository.save(semesters.values());
        courseRepository.save(courses.values());
        studentRepository.save(students.values());

        addTags();
        addCoursesToStudents();
    }

    @Test
    public void itReturnsEmptyTagsFrequencyForStudentWithoutCourses() throws Exception {
        List<TagFrequency> tagsFrequency = studentRepository.computeTagsFrequencyFor(students.get("Student1"));

        assertThat(tagsFrequency, empty());
    }

    @Test
    public void itReturnsTagsFrequencyForStudentWithCourses() throws Exception {
        List<TagFrequency> tagsFrequency = studentRepository.computeTagsFrequencyFor(students.get("Student2"));

        Map<String, Long> actual = new HashMap<>();
        tagsFrequency.forEach(it -> actual.put(it.getTag().getName(), it.getFrequency()));

        Map<String, Long> expected = new HashMap<>();
        expected.put("Tag1", 3L);
        expected.put("Tag2", 2L);
        expected.put("Tag3", 1L);

        assertEquals(expected, actual);
    }

    @Test
    public void itReturnsNullOnFindByUsernameWithUnknownUsername() throws Exception {
        assertNull(studentRepository.findByUsername("unknown"));
    }

    @Test
    public void itFindsStudentByUsername() throws Exception {
        assertEquals(students.get("Student1"), studentRepository.findByUsername("student"));
    }
}
