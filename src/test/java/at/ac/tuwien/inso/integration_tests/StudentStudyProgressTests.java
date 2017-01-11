package at.ac.tuwien.inso.integration_tests;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.study_progress.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.web.servlet.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

import static at.ac.tuwien.inso.utils.IterableUtils.*;
import static java.math.BigDecimal.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class StudentStudyProgressTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private SemesterRepository semesterRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private LecturerRepository lecturerRepository;
    @Autowired
    private StudyPlanRepository studyPlanRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private FeedbackRepository feedbackRepository;

    private Student student;

    private List<Semester> semesters;

    private List<Course> courses;

    @Before
    public void setUp() throws Exception {
        student = studentRepository.save(new Student("123", "student", "mail@uis.at", new UserAccount("student", "pass", Role.STUDENT)));

        prepareSemesters();
        prepareStudyPlans();
        prepareCourses();
        prepareGrades();
        prepareFeedback();
    }

    private void prepareSemesters() {
        semesters = toList(semesterRepository.save(asList(
                new Semester(2016, SemesterType.SummerSemester),
                new Semester(2015, SemesterType.WinterSemester),
                new Semester(2014, SemesterType.SummerSemester),
                new Semester(2014, SemesterType.WinterSemester)
        )));
    }

    private void prepareStudyPlans() {
        List<StudyPlan> studyPlans = toList(studyPlanRepository.save(asList(
                new StudyPlan("study 1", new EctsDistribution(ONE, ONE, ONE)),
                new StudyPlan("study 2", new EctsDistribution(ONE, ONE, ONE))
        )));

        student.addStudyplans(
                new StudyPlanRegistration(studyPlans.get(0), semesters.get(1)),
                new StudyPlanRegistration(studyPlans.get(1), semesters.get(2))
        );
    }

    private void prepareCourses() {
        Subject subject = subjectRepository.save(new Subject("subject 1", ONE));

        courses = toList(courseRepository.save(asList(
                new Course(subject, semesters.get(0)),
                new Course(subject, semesters.get(1)),
                new Course(subject, semesters.get(1)),
                new Course(subject, semesters.get(1)),
                new Course(subject, semesters.get(3)),
                new Course(subject, semesters.get(3)),
                new Course(subject, semesters.get(3))
        )));
        courses.forEach(it -> it.addStudents(student));
    }

    private void prepareGrades() {
        Lecturer lecturer = lecturerRepository.save(new Lecturer("456", "lecturer", "lecturer@uis.at"));

        gradeRepository.save(asList(
                new Grade(courses.get(1), lecturer, student, Mark.EXCELLENT),
                new Grade(courses.get(2), lecturer, student, Mark.FAILED),

                new Grade(courses.get(4), lecturer, student, Mark.SUFFICIENT)
        ));
    }

    private void prepareFeedback() {
        feedbackRepository.save(asList(
                new Feedback(student, courses.get(1)),
                new Feedback(student, courses.get(2)),

                new Feedback(student, courses.get(4)),
                new Feedback(student, courses.get(5))
        ));
    }

    @Test
    public void itShowsStudyProgressForStudent() throws Exception {
        mockMvc.perform(
                get("/student/myCourses").with(user("student").roles(Role.STUDENT.name()))
        ).andExpect(
                model().attribute("studyProgress", new StudyProgress(
                        semesters.get(3).toDto(),
                        asList(
                                new SemesterProgress(semesters.get(3).toDto(), asList(
                                        new CourseRegistration(courses.get(4), CourseRegistrationState.complete_ok),
                                        new CourseRegistration(courses.get(5), CourseRegistrationState.needs_grade),
                                        new CourseRegistration(courses.get(6), CourseRegistrationState.in_progress)
                                )),
                                new SemesterProgress(semesters.get(2).toDto(), emptyList()),
                                new SemesterProgress(semesters.get(1).toDto(), asList(
                                        new CourseRegistration(courses.get(1), CourseRegistrationState.complete_ok),
                                        new CourseRegistration(courses.get(2), CourseRegistrationState.complete_not_ok),
                                        new CourseRegistration(courses.get(3), CourseRegistrationState.needs_feedback)
                                ))
                        )
                ))
        );
    }

}
