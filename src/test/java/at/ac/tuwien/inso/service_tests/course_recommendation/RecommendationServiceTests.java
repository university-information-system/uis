package at.ac.tuwien.inso.service_tests.course_recommendation;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.course_recommendation.impl.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.runners.*;

import java.util.*;

import static java.util.Arrays.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RecommendationServiceTests {

    @Mock
    private Student student;

    @Mock
    private Subject subject;

    @Mock
    private Semester semester;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private SemesterRepository semesterRepository;

    @Mock
    private TagFrequencyScorer tagFrequencyScorer;

    @InjectMocks
    private RecommendationServiceImpl recommendationService;

    private HashMap<String, Semester> semesters = new HashMap<String, Semester>() {
        {
            put("WS16", new Semester(2016, SemesterType.WinterSemester));
        }
    };

    private List<Course> courses = asList(
            new Course(subject, semesters.get("WS16")).addTags(new Tag("tag1")),
            new Course(subject, semesters.get("WS16")).addTags(new Tag("tag2")),
            new Course(subject, semesters.get("WS16")).addTags(new Tag("tag3")),
            new Course(subject, semesters.get("WS16")).addTags(new Tag("tag4")),
            new Course(subject, semesters.get("WS16")).addTags(new Tag("tag5"))
    );

    private Map<Course, Double> scoredCoursesByTagFrequency = new HashMap<Course, Double>() {
        {
            put(courses.get(0), 3.1);
            put(courses.get(2), 7.4);
            put(courses.get(1), 4.3);
            put(courses.get(3), 3.8);
            put(courses.get(4), 5.3);
        }
    };

    @Before
    public void setUp() throws Exception {
        when(courseRepository.findAllRecommendableForStudent(student)).thenReturn(courses);
        when(semesterRepository.findFirstByOrderByIdDesc()).thenReturn(semesters.get("WS16"));
        when(tagFrequencyScorer.score(courses, student)).thenReturn(scoredCoursesByTagFrequency);
    }

    @Test
    public void itRecommendsCoursesByTagScoring() throws Exception {
        List<Course> recommendedCourses = recommendationService.recommendCourses(student);

        List<Course> expected = asList(
                courses.get(2), courses.get(4), courses.get(1), courses.get(3), courses.get(0)
        );

        assertEquals(expected, recommendedCourses);
    }
}
