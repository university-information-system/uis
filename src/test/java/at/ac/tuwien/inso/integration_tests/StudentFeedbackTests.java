package at.ac.tuwien.inso.integration_tests;

import static at.ac.tuwien.inso.entity.Feedback.Type.DISLIKE;
import static at.ac.tuwien.inso.entity.Feedback.Type.LIKE;
import static at.ac.tuwien.inso.utils.IterableUtils.toList;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.inso.controller.student.forms.FeedbackForm;
import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Feedback;
import at.ac.tuwien.inso.entity.Role;
import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.entity.SemesterType;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.entity.UserAccount;
import at.ac.tuwien.inso.repository.CourseRepository;
import at.ac.tuwien.inso.repository.FeedbackRepository;
import at.ac.tuwien.inso.repository.GradeRepository;
import at.ac.tuwien.inso.repository.LecturerRepository;
import at.ac.tuwien.inso.repository.SemesterRepository;
import at.ac.tuwien.inso.repository.StudentRepository;
import at.ac.tuwien.inso.repository.SubjectRepository;
import at.ac.tuwien.inso.repository.UisUserRepository;
import at.ac.tuwien.inso.service.student_subject_prefs.StudentSubjectPreferenceStore;

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

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private LecturerRepository lecturerRepository;
    @Autowired
    private StudentSubjectPreferenceStore studentSubjectPreferenceStore;

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
        Semester semester = semesterRepository.save(new Semester(2016, SemesterType.WinterSemester));

        courses = toList(courseRepository.save(asList(
                new Course(subject, semester),
                new Course(subject, semester).addStudents(student)
        )));

        studentSubjectPreferenceStore.studentRegisteredCourse(student, courses.get(1));
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
