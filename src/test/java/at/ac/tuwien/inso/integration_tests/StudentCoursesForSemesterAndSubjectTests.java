package at.ac.tuwien.inso.integration_tests;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.entity.SemesterType;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.repository.CourseRepository;
import at.ac.tuwien.inso.repository.SemesterRepository;
import at.ac.tuwien.inso.repository.SubjectRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static java.util.Arrays.asList;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class StudentCoursesForSemesterAndSubjectTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    private Subject subject = new Subject("maths", new BigDecimal(6.0));
    private Semester ws2016 = new Semester(2016, SemesterType.WinterSemester);

    @Before
    public void setUp() {
        subjectRepository.save(subject);
        semesterRepository.save(ws2016);
    }

    @Test
    public void getTheOnlyCourseOfCurrentSemesterForSubjectTest() throws Exception {

        // given only one course for the ws2016 of the subject "maths"
        Course course = courseRepository.save(new Course(subject, ws2016));

        // the user should be redirected directly to the course page
        mockMvc.perform(
                get("/student/courses/semester/subject")
                        .with(user("student").roles("STUDENT"))
                        .param("subjectId", subject.getId().toString())
        ).andExpect(
                redirectedUrl("/student/courses/" + course.getId())
        );
    }

    @Test
    public void getManyCoursesOfCurrentSemesterForSubjectTest() throws Exception {

        // given two courses for the ws2016 of the subject "maths"
        Course course1 = courseRepository.save(new Course(subject, ws2016));
        Course course2 = courseRepository.save(new Course(subject, ws2016));

        // the user should see a list of courses of this subject
        mockMvc.perform(
                get("/student/courses/semester/subject")
                        .with(user("student").roles("STUDENT"))
                        .param("subjectId", subject.getId().toString())
        ).andExpect(
                view().name("/student/courses-for-subject")
        ).andExpect(
                model().attribute("coursesForSemesterAndSubject", asList(course1, course2))
        );
    }

}
