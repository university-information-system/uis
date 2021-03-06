package at.ac.tuwien.inso.service_tests.course_recommendation;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.entity.SemesterType;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.entity.Tag;
import at.ac.tuwien.inso.repository.CourseRepository;
import at.ac.tuwien.inso.repository.SemesterRepository;
import at.ac.tuwien.inso.service.course_recommendation.CourseScorer;
import at.ac.tuwien.inso.service.course_recommendation.filters.CourseRelevanceFilter;
import at.ac.tuwien.inso.service.course_recommendation.impl.RecommendationServiceImpl;
import at.ac.tuwien.inso.service.course_recommendation.normalization.CourseNormalizer;

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
    private CourseNormalizer courseNormalizer;

    private List<CourseRelevanceFilter> filters = asList(
            mock(CourseRelevanceFilter.class),
            mock(CourseRelevanceFilter.class)
    );

    private List<CourseScorer> scorers = asList(
            mock(CourseScorer.class),
            mock(CourseScorer.class)
    );

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

        scorers.forEach(it -> {
            when(it.score(courses, student)).thenReturn(scoredCoursesByTagFrequency);
            when(it.weight()).thenReturn(1.0);
        });
        recommendationService.setCourseScorers(scorers);

        filters.forEach(it -> when(it.filter(courses, student)).thenReturn(courses));
        recommendationService.setCourseRelevanceFilters(filters);
    }

    @Test
    public void itRecommendsCoursesByScoring() throws Exception {
        List<Course> recommendedCourses = recommendationService.recommendCourses(student);

        List<Course> expected = asList(
                courses.get(2), courses.get(4), courses.get(1), courses.get(3), courses.get(0)
        );

        assertEquals(expected, recommendedCourses);
    }

    @Test
    public void itFiltersCoursesBeforeRecommendingThem() throws Exception {
        List<Course> coursesAfterFilter1 = courses.stream().skip(1).collect(Collectors.toList());
        when(filters.get(0).filter(courses, student)).thenReturn(coursesAfterFilter1);

        List<Course> coursesAfterFilter2 = coursesAfterFilter1.stream().skip(1).collect(Collectors.toList());
        when(filters.get(1).filter(coursesAfterFilter1, student)).thenReturn(coursesAfterFilter2);

        scorers.forEach(it -> when(it.score(coursesAfterFilter2, student)).thenReturn(scoredCoursesByTagFrequency));

        List<Course> recommendedCourses = recommendationService.recommendCourses(student);

        assertThat(recommendedCourses, not(hasItems(courses.get(0), courses.get(1))));
    }
}
