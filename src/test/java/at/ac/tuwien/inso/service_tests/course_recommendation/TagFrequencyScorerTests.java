package at.ac.tuwien.inso.service_tests.course_recommendation;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.entity.Tag;
import at.ac.tuwien.inso.repository.CourseRepository;
import at.ac.tuwien.inso.repository.FeedbackRepository;
import at.ac.tuwien.inso.repository.GradeRepository;
import at.ac.tuwien.inso.repository.StudentRepository;
import at.ac.tuwien.inso.service.course_recommendation.impl.TagFrequencyCalculatorImpl;
import at.ac.tuwien.inso.service.course_recommendation.impl.TagFrequencyScorer;

@RunWith(MockitoJUnitRunner.class)
public class TagFrequencyScorerTests {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private GradeRepository gradeRepository;

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private Student student;

    @Mock
    private Subject subject;

    @Mock
    private Semester semester;

    @Mock
    private Lecturer lecturer;

    @Mock
    private TagFrequencyCalculatorImpl tagFrequencyCalculator;

    @InjectMocks
    private TagFrequencyScorer tagFrequencyScorer;

    private List<Course> courses = asList(
            new Course(subject, semester).addTags(new Tag("tag2")),
            new Course(subject, semester).addTags(new Tag("tag1"), new Tag("tag4"), new Tag("tag2")),
            new Course(subject, semester).addTags(new Tag("tag3")),
            new Course(subject, semester).addTags(new Tag("tag2"), new Tag("tag3")),
            new Course(subject, semester).addTags(new Tag("tag5"), new Tag("tag2"))
    );

    private Map<Tag, Double> expectedTagFrequencies = new HashMap<Tag, Double>() {
        {
            put(new Tag("tag1"), 0.5);
            put(new Tag("tag2"), 4.3);
            put(new Tag("tag3"), 3.1);
            put(new Tag("tag4"), 0.5);
            put(new Tag("tag5"), -0.5);
        }
    };

    private Map<Course, Double> expectedScoredCourses = new HashMap<Course, Double>() {
        {
            put(courses.get(3), expectedTagFrequencies.get(new Tag("tag3")) + expectedTagFrequencies.get(new Tag("tag2")));
            put(courses.get(1), expectedTagFrequencies.get(new Tag("tag1")) + expectedTagFrequencies.get(new Tag("tag4")) + expectedTagFrequencies.get(new Tag("tag2")));
            put(courses.get(0), expectedTagFrequencies.get(new Tag("tag2")));
            put(courses.get(2), expectedTagFrequencies.get(new Tag("tag3")));
            put(courses.get(4), expectedTagFrequencies.get(new Tag("tag5")) + expectedTagFrequencies.get(new Tag("tag2")));
        }
    };

    @Before
    public void setUp() throws Exception {
        when(courseRepository.findAllForStudent(student)).thenReturn(courses);
        when(tagFrequencyCalculator.calculate(student)).thenReturn(expectedTagFrequencies);
    }


    @Test
    public void itScoresCoursesByTagFrequency() throws Exception {
        Map<Course, Double> scoredCourses = tagFrequencyScorer.score(courses, student);

        assertEquals(expectedScoredCourses, scoredCourses);
    }
}
