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
import at.ac.tuwien.inso.entity.Feedback;
import at.ac.tuwien.inso.entity.Feedback.Type;
import at.ac.tuwien.inso.entity.Grade;
import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.entity.Mark;
import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.entity.Tag;
import at.ac.tuwien.inso.repository.CourseRepository;
import at.ac.tuwien.inso.repository.FeedbackRepository;
import at.ac.tuwien.inso.repository.GradeRepository;
import at.ac.tuwien.inso.repository.StudentRepository;
import at.ac.tuwien.inso.service.course_recommendation.impl.TagFrequencyCalculatorImpl;

@RunWith(MockitoJUnitRunner.class)
public class TagFrequencyCalculatorTests {

    private static final Map<Mark, Double> gradeWeights = new HashMap<Mark, Double>() {
        {
            put(Mark.EXCELLENT, 0.5);
            put(Mark.GOOD, 0.3);
            put(Mark.SATISFACTORY, 0.1);
            put(Mark.SUFFICIENT, 0.1);
            put(Mark.FAILED, -0.5);
        }
    };
    private static final Map<Feedback.Type, Double> feedbackWeights = new HashMap<Feedback.Type, Double>() {
        {
            put(Type.LIKE, 1.0);
            put(Type.DISLIKE, -1.0);
        }
    };
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
    @InjectMocks
    private TagFrequencyCalculatorImpl tagFrequencyCalculator;
    private List<Course> courses = asList(
            new Course(subject, semester).addTags(new Tag("tag2")),
            new Course(subject, semester).addTags(new Tag("tag1"), new Tag("tag4"), new Tag("tag2")),
            new Course(subject, semester).addTags(new Tag("tag3")),
            new Course(subject, semester).addTags(new Tag("tag2"), new Tag("tag3")),
            new Course(subject, semester).addTags(new Tag("tag5"), new Tag("tag2"))
    );

    private List<Grade> grades = asList(
            new Grade(courses.get(0), lecturer, student, Mark.GOOD),
            new Grade(courses.get(1), lecturer, student, Mark.EXCELLENT),
            new Grade(courses.get(2), lecturer, student, Mark.SUFFICIENT),
            new Grade(courses.get(4), lecturer, student, Mark.FAILED)
    );

    private List<Feedback> feedbacks = asList(
            new Feedback(student, courses.get(0), Type.LIKE),
            new Feedback(student, courses.get(1), Type.DISLIKE),
            new Feedback(student, courses.get(3), Type.LIKE),
            new Feedback(student, courses.get(4), Type.DISLIKE)
    );

    private Map<Tag, Double> tagFrequencies = new HashMap<Tag, Double>() {
        {
            put(new Tag("tag1"), 1.0);
            put(new Tag("tag2"), 4.0);
            put(new Tag("tag3"), 2.0);
            put(new Tag("tag4"), 1.0);
            put(new Tag("tag5"), 1.0);
        }
    };

    private Map<Tag, Double> tagFrequenciesWithGrades = new HashMap<Tag, Double>() {
        {
            put(new Tag("tag1"), gradeWeights.get(Mark.EXCELLENT));
            put(new Tag("tag2"), gradeWeights.get(Mark.GOOD) + gradeWeights.get(Mark.EXCELLENT) + gradeWeights.get(Mark.FAILED));
            put(new Tag("tag3"), gradeWeights.get(Mark.SUFFICIENT));
            put(new Tag("tag4"), gradeWeights.get(Mark.EXCELLENT));
            put(new Tag("tag5"), gradeWeights.get(Mark.FAILED));
        }
    };

    private Map<Tag, Double> getTagFrequenciesWithFeedback = new HashMap<Tag, Double>() {
        {
            put(new Tag("tag1"), feedbackWeights.get(Type.DISLIKE));
            put(new Tag("tag2"), feedbackWeights.get(Type.LIKE) + feedbackWeights.get(Type.DISLIKE) + feedbackWeights.get(Type.LIKE) + feedbackWeights.get(Type.DISLIKE));
            put(new Tag("tag3"), feedbackWeights.get(Type.LIKE));
            put(new Tag("tag4"), feedbackWeights.get(Type.DISLIKE));
            put(new Tag("tag5"), feedbackWeights.get(Type.DISLIKE));
        }
    };

    private Map<Tag, Double> expectedTagFrequencies = new HashMap<Tag, Double>() {
        {
            put(new Tag("tag1"), tagFrequencies.get(new Tag("tag1")) + tagFrequenciesWithGrades.get(new Tag("tag1")) + getTagFrequenciesWithFeedback.get(new Tag("tag1")));
            put(new Tag("tag2"), tagFrequencies.get(new Tag("tag2")) + tagFrequenciesWithGrades.get(new Tag("tag2")) + getTagFrequenciesWithFeedback.get(new Tag("tag2")));
            put(new Tag("tag3"), tagFrequencies.get(new Tag("tag3")) + tagFrequenciesWithGrades.get(new Tag("tag3")) + getTagFrequenciesWithFeedback.get(new Tag("tag3")));
            put(new Tag("tag4"), tagFrequencies.get(new Tag("tag4")) + tagFrequenciesWithGrades.get(new Tag("tag4")) + getTagFrequenciesWithFeedback.get(new Tag("tag4")));
            put(new Tag("tag5"), tagFrequencies.get(new Tag("tag5")) + tagFrequenciesWithGrades.get(new Tag("tag5")) + getTagFrequenciesWithFeedback.get(new Tag("tag5")));
        }
    };

    @Before
    public void setUp() throws Exception {
        when(courseRepository.findAllForStudent(student)).thenReturn(courses);
        when(gradeRepository.findAllOfStudent(student)).thenReturn(grades);
        when(feedbackRepository.findAllOfStudent(student)).thenReturn(feedbacks);
    }

    @Test
    public void itCalculatesTagFrequencies() throws Exception {
        Map<Tag, Double> calculatedTagFrequencies = tagFrequencyCalculator.calculate(student);

        assertEquals(expectedTagFrequencies, calculatedTagFrequencies);
    }
}
