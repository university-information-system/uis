package at.ac.tuwien.inso.dao_tests;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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
        tagRepository.save(tags.values());
        subjectRepository.save(subjects.values());
        semesterRepository.save(semesters.values());
        courseRepository.save(courses.values());

        addTags();
    }

    @Test
    public void itReturnsCoursesForCurrentSemester() throws Exception {
        List<Course> actual = courseRepository.findAllByCurrentSemesterWithTags();

        assertThat(actual, CoreMatchers.hasItems(courses.get("Course1"), courses.get("Course2"), courses.get("Course3")));
    }
}
