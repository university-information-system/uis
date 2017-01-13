package at.ac.tuwien.inso.service_tests;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.exception.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.*;
import at.ac.tuwien.inso.service.impl.*;
import at.ac.tuwien.inso.service.student_subject_prefs.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.runners.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FeedbackServiceTests {

    @Mock
    private CourseRepository courseRepository;
    @Mock
    private FeedbackRepository feedbackRepository;
    @Mock
    private StudentSubjectPreferenceStore studentSubjectPreferenceStore;

    @InjectMocks
    private FeedbackService feedbackService = new FeedbackServiceImpl();

    private Student student = new Student("123", "student", "mail@uis.at");
    private Course course = new Course(mock(Subject.class), mock(Semester.class));

    private Feedback feedback = new Feedback(student, course);

    private Feedback savedFeedback = mock(Feedback.class);

    @Before
    public void setUp() throws Exception {
        when(feedbackRepository.save(feedback)).thenReturn(savedFeedback);

        when(feedbackRepository.exists(feedback)).thenReturn(false);
        when(courseRepository.existsCourseRegistration(student, course)).thenReturn(true);
    }

    @Test
    public void onSaveItPersistsFeedback() throws Exception {
        assertThat(feedbackService.save(feedback), equalTo(savedFeedback));
    }

    @Test
    public void onSaveItNotifiesStudentSubjectPreferenceStore() throws Exception {
        feedbackService.save(feedback);

        verify(studentSubjectPreferenceStore).studentGaveCourseFeedback(student, feedback);
    }

    @Test(expected = ActionNotAllowedException.class)
    public void onSaveWithAlreadyExistingFeedbackItThrows() throws Exception {
        when(feedbackRepository.exists(feedback)).thenReturn(true);

        feedbackService.save(feedback);
    }

    @Test(expected = ActionNotAllowedException.class)
    public void onSaveWithFeedbackForUnregisteredCourseItThrows() throws Exception {
        when(courseRepository.existsCourseRegistration(student, course)).thenReturn(false);

        feedbackService.save(feedback);
    }
}
