package at.ac.tuwien.inso.integration_tests;

import at.ac.tuwien.inso.controller.student.forms.*;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.*;
import org.springframework.transaction.annotation.*;

import java.math.*;
import java.util.*;
import java.util.stream.*;

import static at.ac.tuwien.inso.entity.Feedback.Type.*;
import static at.ac.tuwien.inso.utils.IterableUtils.*;
import static java.util.Arrays.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class StudentFeedbackTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UisUserRepository uisUserRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private SemesterRepository semesterRepository;
    @Autowired
    private FeedbackRepository feedbackRepository;

    private Student student;
    private List<Course> courses;

    @Before
    public void setUp() {
        prepareStudent();
        prepareCourses();
    }

    private void prepareStudent() {
        student = uisUserRepository.save(new Student("1", "student", "email", new UserAccount("student", "pass", Role.STUDENT)));
    }

    private void prepareCourses() {
        Subject subject = subjectRepository.save(new Subject("subject", BigDecimal.ONE));
        Semester semester = semesterRepository.save(new Semester("current"));

        courses = toList(courseRepository.save(asList(
                new Course(subject, semester),
                new Course(subject, semester).addStudents(student)
        )));
    }

    @Test
    public void itPersistsFeedbackFromStudentForCourseHeIsRegisteredTo() throws Exception {
        FeedbackForm form = new FeedbackForm(courses.get(1).getId(), false, "some description");

        mockMvc.perform(
                giveFeedback(form)
        ).andExpect(
                feedbackCreated(form)
        ).andExpect(
                redirectedUrl("/student/myCourses")
        ).andExpect(
                flash().attribute("flashMessage", "student.my.courses.feedback.success")
        );
    }

    private MockHttpServletRequestBuilder giveFeedback(FeedbackForm form) {
        return post("/student/feedback")
                .param("course", form.getCourse().toString())
                .param("suggestions", form.getSuggestions())
                .param("like", form.isLike().toString())
                .with(csrf())
                .with(user(student.getAccount()));
    }

    private ResultMatcher feedbackCreated(FeedbackForm form) {
        return result -> {
            List<Feedback> feedbacks = feedbackRepository.findAllOfStudent(student);
            assertThat(feedbacks, hasSize(1));

            Feedback feedback = feedbacks.get(0);
            assertThat(feedback.getSuggestions(), equalTo(form.getSuggestions()));
            assertThat(feedback.getType(), equalTo(form.isLike() ? LIKE : DISLIKE));
        };
    }

    @Test
    public void itDoesNotPersistsFeedbackFromStudentForCourseHeIsNotRegisteredTo() throws Exception {
        FeedbackForm form = new FeedbackForm(courses.get(0).getId(), true, "some description");

        mockMvc.perform(
                giveFeedback(form)
        ).andExpect(
                feedbackNotCreated()
        ).andExpect(
                status().isForbidden()
        );
    }

    private ResultMatcher feedbackNotCreated() {
        return result -> assertThat(feedbackRepository.findAllOfStudent(student), empty());
    }

    @Test
    public void itRespondsNotFoundOnFeedbackForUnknownCourse() throws Exception {
        FeedbackForm form = new FeedbackForm(-1L, false, "some description");

        mockMvc.perform(
                giveFeedback(form)
        ).andExpect(
                status().isNotFound()
        );
    }

    @Test
    public void itRespondsForbiddenOnMultipleFeedbackSubmissionsForSameCourse() throws Exception {
        feedbackRepository.save(new Feedback(student, courses.get(1)));

        FeedbackForm form = new FeedbackForm(courses.get(1).getId(), true, "some description");

        mockMvc.perform(
                giveFeedback(form)
        ).andExpect(
                status().isForbidden()
        );
    }

    @Test
    public void itDoesNotPersistFeedbackWithTooLongSuggestions() throws Exception {
        FeedbackForm form = new FeedbackForm(courses.get(1).getId(), true, tooLongSuggestions());

        mockMvc.perform(
                giveFeedback(form)
        ).andExpect(
                feedbackNotCreated()
        ).andExpect(
                status().isBadRequest()
        );
    }

    private String tooLongSuggestions() {
        StringBuilder builder = new StringBuilder();
        IntStream.range(0, 1025).forEach(it -> builder.append("a"));
        return builder.toString();
    }
}
