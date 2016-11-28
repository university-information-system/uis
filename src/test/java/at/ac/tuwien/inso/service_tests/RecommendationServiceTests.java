package at.ac.tuwien.inso.service_tests;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.exception.BusinessObjectNotFoundException;
import at.ac.tuwien.inso.exception.ValidationException;
import at.ac.tuwien.inso.repository.CourseRepository;
import at.ac.tuwien.inso.repository.StudentRepository;
import at.ac.tuwien.inso.repository.StudyPlanRepository;
import at.ac.tuwien.inso.repository.SubjectForStudyPlanRepository;
import at.ac.tuwien.inso.repository.utils.TagFrequency;
import at.ac.tuwien.inso.service.RecommendationService;
import at.ac.tuwien.inso.service.SubjectService;
import at.ac.tuwien.inso.service.impl.RecommendationServiceImpl;
import at.ac.tuwien.inso.service.impl.StudyPlanServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecommendationServiceTests {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private RecommendationServiceImpl recommendationService;

    @Mock
    private Student student;

    @Mock
    private Subject subject;

    @Mock
    private Semester semester;

    private List<TagFrequency> tagFrequencies = asList(
            new TagFrequency(new Tag("tag1"), 3),
            new TagFrequency(new Tag("tag2"), 1),
            new TagFrequency(new Tag("tag3"), 2)
    );

    private List<Course> courses = asList(
            new Course(subject, semester).addTags(new Tag("tag2")),
            new Course(subject, semester).addTags(new Tag("tag1"), new Tag("tag4")),
            new Course(subject, semester).addTags(new Tag("tag3")),
            new Course(subject, semester).addTags(new Tag("tag2"), new Tag("tag3"))
    );

    @Before
    public void setUp() throws Exception {
        when(studentRepository.computeTagsFrequencyFor(student)).thenReturn(tagFrequencies);
        when(courseRepository.findAllByCurrentSemesterWithTags()).thenReturn(courses);
    }

    @Test
    public void itRecommendsCoursesByTagScoring() throws Exception {
        List<Course> recommendedCourses = recommendationService.recommendCourses(student);

        List<Course> expected = asList(
                courses.get(1), courses.get(3), courses.get(2), courses.get(0)
        );

        assertEquals(expected, recommendedCourses);
    }
}
