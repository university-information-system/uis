package at.ac.tuwien.inso.integration_tests;

import at.ac.tuwien.inso.controller.student.forms.*;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.student_subject_prefs.*;
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

    @Test
    public void lecturerShouldSeeFeedback() throws Exception {

        // given course ase held by lecturer1 and students student1 and student2 with grades in ase
        Lecturer lecturer1 = lecturerRepository.save(new Lecturer("l0002", "Lecturer 1", "email", new UserAccount("lecturer1", "pass", Role.LECTURER)));
        Student st1 = studentRepository.save(new Student("st1", "Student2", "st1@ude.nt", new UserAccount("st1", "pass", Role.STUDENT)));
        Student st2 = studentRepository.save(new Student("st2", "Student3", "st2@ude.nt", new UserAccount("st2", "pass", Role.STUDENT)));
        Subject ase = subjectRepository.save(new Subject("ASE", new BigDecimal(6.0)));
        Semester ws2016 = semesterRepository.save(new Semester(2016, SemesterType.WinterSemester));
        Course aseWS2016 = courseRepository.save(new Course(ase, ws2016).addStudents(st1,st2));
        gradeRepository.save(new Grade(aseWS2016, lecturer1, st1, Mark.EXCELLENT));
        gradeRepository.save(new Grade(aseWS2016, lecturer1, st2, Mark.GOOD));

        // when student1 and student2 give feedback
        Feedback feedback1 = new Feedback(
                st1,
                aseWS2016,
                Feedback.Type.DISLIKE, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam nec enim ligula. " +
                "Sed eget posuere tellus. Aenean fermentum maximus tempor. Ut ultricies dapibus nulla vitae mollis. " +
                "Suspendisse a nunc nisi. Sed ut sapien eu odio sodales laoreet eu ac turpis. " +
                "In id sapien id ante sollicitudin consectetur at laoreet mi. Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Suspendisse quam sem, ornare eget pellentesque sit amet, tincidunt id metus. Sed scelerisque neque sed laoreet elementum. " +
                "Integer eros neque, vulputate a hendrerit at, ullamcorper in orci. Donec sit amet risus hendrerit, hendrerit magna non, dapibus nibh. " +
                "Suspendisse sed est feugiat, dapibus ante non, aliquet neque. Cras magna sapien, pharetra ut ante ut, malesuada hendrerit erat. " +
                "Mauris fringilla mattis dapibus. Nullam iaculis nunc in tortor gravida, id tempor justo elementum.");
        Feedback feedback2 = new Feedback(
                st2,
                aseWS2016,
                Feedback.Type.LIKE, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam nec enim ligula. " +
                "Sed eget posuere tellus. Aenean fermentum maximus tempor. Ut ultricies dapibus nulla vitae mollis. " +
                "Suspendisse a nunc nisi. Sed ut sapien eu odio sodales laoreet eu ac turpis. " +
                "In id sapien id ante sollicitudin consectetur at laoreet mi. Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Suspendisse quam sem, ornare eget pellentesque sit amet, tincidunt id metus. Sed scelerisque neque sed laoreet elementum. " +
                "Integer eros neque, vulputate a hendrerit at, ullamcorper in orci. Donec sit amet risus hendrerit, hendrerit magna non, dapibus nibh. " +
                "Suspendisse sed est feugiat, dapibus ante non, aliquet neque. Cras magna sapien, pharetra ut ante ut, malesuada hendrerit erat. " +
                "Mauris fringilla mattis dapibus. Nullam iaculis nunc in tortor gravida, id tempor justo elementum.");

        feedbackRepository.save(asList(feedback1, feedback2));

        // the lecturer should see the feedback
        mockMvc.perform(
                get("/lecturer/course-details/feedback")
                        .param("courseId", aseWS2016.getId().toString())
                        .with(user("lecturer1").roles(Role.LECTURER.name()))
                        .with(csrf())
        ).andExpect(
                model().attribute("course", aseWS2016)
        ).andExpect(
                model().attribute("feedbacks", asList(feedback1, feedback2))
        );

    }

    private String tooLongSuggestions() {
        StringBuilder builder = new StringBuilder();
        IntStream.range(0, 1025).forEach(it -> builder.append("a"));
        return builder.toString();
    }
}
