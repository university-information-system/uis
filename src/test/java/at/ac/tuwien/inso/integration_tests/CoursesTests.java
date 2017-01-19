package at.ac.tuwien.inso.integration_tests;


import at.ac.tuwien.inso.controller.lecturer.forms.*;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.impl.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.data.domain.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.web.servlet.*;
import org.springframework.transaction.annotation.*;

import java.math.*;
import java.util.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class CoursesTests {

    UserAccount user1 = new UserAccount("lecturer1", "pass", Role.LECTURER);
    Lecturer lecturer1 = new Lecturer("l0001", "Lecturer 1", "email", user1);
    Lecturer lecturer2 = new Lecturer("l0002", "Lecturer 2", "email", new UserAccount("lecturer2", "pass", Role.LECTURER));
    Lecturer lecturer3 = new Lecturer("l0003", "Lecturer 3", "email", new UserAccount("lecturer3", "pass", Role.LECTURER));
    Student student = new Student("s1234", "Student", "student@uis.at", new UserAccount("student", "pass", Role.STUDENT));
    Semester ss2016 = new Semester(2016, SemesterType.SummerSemester);
    Semester ws2016 = new Semester(2016, SemesterType.WinterSemester);
    Subject calculus = new Subject("Calculus", new BigDecimal(3.0));
    Subject sepm = new Subject("SEPM", new BigDecimal(6.0));
    Subject ase = new Subject("ASE", new BigDecimal(6.0));
    Course sepmSS2016 = new Course(sepm, ss2016);
    Course sepmWS2016 = new Course(sepm, ws2016);
    Course aseWS2016 = new Course(ase, ws2016);
    Course calculusWS2016 = new Course(calculus, ws2016);
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
    private StudentRepository studentRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private Messages messages;

    private List<Course> expectedCourses;
    private List<Course> expectedCoursesForLecturer1;
    private List<Course> expectedCoursesForLecturer2;
    private List<Course> expectedCoursesForLecturer3;

    @Before
    public void setUp() {
        student = studentRepository.save(student);

        lecturerRepository.save(lecturer1);
        lecturerRepository.save(lecturer2);
        lecturerRepository.save(lecturer3);

        semesterRepository.save(ss2016);
        semesterRepository.save(ws2016);

        subjectRepository.save(calculus);
        calculus.addLecturers(lecturer3);
        subjectRepository.save(sepm);
        sepm.addLecturers(lecturer1);
        subjectRepository.save(ase);
        ase.addLecturers(lecturer1, lecturer2);
        
        calculusWS2016.addStudents(student);
        
        sepmSS2016 = courseRepository.save(sepmSS2016);
        sepmWS2016 = courseRepository.save(sepmWS2016);
        aseWS2016 = courseRepository.save(aseWS2016);
        calculusWS2016 = courseRepository.save(calculusWS2016);

        expectedCourses = Arrays.asList(sepmWS2016, aseWS2016, calculusWS2016);
        expectedCoursesForLecturer1 = Arrays.asList(sepmWS2016, aseWS2016);
        expectedCoursesForLecturer3 = Arrays.asList(aseWS2016);
        expectedCoursesForLecturer2 = Arrays.asList(calculusWS2016);

        tagRepository.save(asList(
                new Tag("Computer Science"),
                new Tag("Math"),
                new Tag("Fun"),
                new Tag("Easy"),
                new Tag("Difficult")
        ));
    }

    @Test
    public void itListsAllCoursesForCurrentSemester() throws Exception {
        mockMvc.perform(
                get("/student/courses").with(user("student").roles("STUDENT"))
        ).andExpect(
                responseHasCourses(expectedCourses)
        ).andExpect(
                model().attributeExists("recommendedCourses")
        );
    }

    private ResultMatcher responseHasCourses(List<Course> courses) {
        return result -> {
            Page<Course> page = (Page<Course>) result.getModelAndView().getModel().get("allCourses");
            assertThat(page.getContent(), containsInAnyOrder(courses.toArray()));
        };
    }

    @Test
    public void itListsAllCoursesForCurrentSemesterWithNameFilterNoString() throws Exception {
        mockMvc.perform(
                get("/student/courses?search=").with(user("student").roles("STUDENT"))
        ).andExpect(
                responseHasCourses(expectedCourses)
        );
    }

    @Test
    public void itListsAllCoursesForCurrentSemesterWithNameFilter() throws Exception {
        mockMvc.perform(
                get("/student/courses?search=sep").with(user("student").roles("STUDENT"))
        ).andExpect(
                responseHasCourses(singletonList(sepmWS2016))
        );
    }

    @Test
    public void itListsAllCoursesForCurrentSemesterAndLecturer() throws Exception {

        mockMvc.perform(
                get("/lecturer/courses").with(user(user1))
        ).andExpect(
                model().attribute("allCourses", expectedCoursesForLecturer1)
        );
    }

    @Test
    public void itCreatesCourse() throws Exception {
        Course course = new Course(sepm, ws2016);
        AddCourseForm form = new AddCourseForm(course);
        form.setInitialTags(tagRepository.findAll());
        form.getActiveAndInactiveTags().get(0).setActive(true);
        mockMvc.perform(
                post("/lecturer/addCourse").with(user("lecturer").roles(Role.LECTURER.name()))
                        .content(form.toString())
                        .param("subjectId", form.getCourse().getSubject().getId().toString())
                        .with(csrf())
        ).andExpect(
                redirectedUrl("/lecturer/courses")
        ).andExpect(
                flash().attributeExists("flashMessageNotLocalized")
        );
    }

    @Test
    public void itEditsCourse() throws Exception {
        AddCourseForm form = new AddCourseForm(sepmWS2016);
        form.setInitialTags(tagRepository.findAll());
        form.setInitialActiveTags(sepmWS2016.getTags());
        String testDescription = "TEST DESCRIPTION";
        form.getCourse().setDescription(testDescription);
        String expected = "Changes for course \"" + form.getCourse().getSubject().getName() + "\" updated";
        mockMvc.perform(
                post("/lecturer/editCourse").with(user("lecturer").roles(Role.LECTURER.name()))
                        .content(form.toString())
                        .param("courseId", form.getCourse().getId().toString())
                        .with(csrf())
        ).andExpect(
                redirectedUrl("/lecturer/courses")
        ).andExpect(
                flash().attributeExists("flashMessageNotLocalized")
        ).andExpect(
                flash().attribute("flashMessageNotLocalized", expected)
        );
    }

    @Test
    public void lecturersShouldSeeRegisteredStudentsToCourse() throws Exception {
        // given lecturer1 and lecturer2 of the ase course

        // when a student has registered to the ase course
        aseWS2016.addStudents(student);

        //lecturer1 should see the registered student
        mockMvc.perform(
                get("/lecturer/course-details/registrations")
                        .param("courseId", aseWS2016.getId().toString())
                        .with(user("lecturer1").roles(Role.LECTURER.name()))
                        .with(csrf())
        ).andExpect(
                model().attribute("course", aseWS2016)
        ).andExpect(
                model().attribute("students", asList(student))
        );

        //lecturer2 should see the registered student too
        mockMvc.perform(
                get("/lecturer/course-details/registrations")
                        .param("courseId", aseWS2016.getId().toString())
                        .with(user("lecturer2").roles(Role.LECTURER.name()))
                        .with(csrf())
        ).andExpect(
                model().attribute("course", aseWS2016)
        ).andExpect(
                model().attribute("students", asList(student))
        );

    }

    @Test
    public void onCourseDetailsForStudentWithUnknownCourseItReturnsError() throws Exception {
        mockMvc.perform(
                get("/student/courses/100000").with(user(student.getAccount()))
        ).andExpect(
                status().isNotFound()
        );
    }

    @Test
    public void itShowsCourseDetailsForStudent() throws Exception {
        mockMvc.perform(
                get("/student/courses/" + sepmSS2016.getId()).with(user(student.getAccount()))
        ).andExpect(
                model().attribute("course", hasProperty("id", equalTo(sepmSS2016.getId())))
        );
    }
    
    
    @Test
    public void testRemoveCourseWithoutStudentsOrGradesByIdSuccessfullyRemovesCourse() throws Exception{
    	Course c = aseWS2016;
    	assertTrue(courseRepository.exists(c.getId()));
    	mockMvc.perform(
    			post("/lecturer/editCourse/remove?courseId="+c.getId())
                        .with(user("lecturer3").roles(Role.LECTURER.name()))
                        .with(csrf())
    	).andExpect(
                redirectedUrl("/lecturer/courses")
        );
    	assertTrue(!courseRepository.exists(c.getId()));
    }
    
    @Test
    public void testRemoveCourseWithNoIdGetsError() throws Exception{
    	
    	mockMvc.perform(
    			get("/lecturer/editCourse/remove?courseId=")
    			.with(user("lecturer3").roles(Role.LECTURER.name()))	
    	).andExpect(
                redirectedUrl(null)
        );
    }
    
    @Test
    public void testRemoveCourseWithStudentsDoesNotWork() throws Exception{
    	
    	Course c = calculusWS2016;
    	assertTrue(courseRepository.exists(c.getId()));
    	mockMvc.perform(
                post("/lecturer/editCourse/remove?courseId="+c.getId())
                        .with(user("lecturer3").roles(Role.LECTURER.name()))
                        .with(csrf())
    	).andExpect(
                redirectedUrl("/lecturer/courses")
        );
    	assertTrue(courseRepository.exists(c.getId()));
    }
    
    @Test
    public void testRemoveCourseWithGradesDoesNotWork() throws Exception{
    	Course c = aseWS2016;
    	Grade grade = new Grade(c, lecturer3, student, Mark.GOOD);
    	gradeRepository.save(grade);
    	assertTrue(courseRepository.exists(c.getId()));
    	assertTrue(!gradeRepository.findByCourseId(c.getId()).isEmpty());
    	mockMvc.perform(
    			post("/lecturer/editCourse/remove?courseId="+c.getId())
                        .with(user("lecturer3").roles(Role.LECTURER.name()))
                        .with(csrf())
    	).andExpect(
                redirectedUrl("/lecturer/courses")
        );
    	assertTrue(courseRepository.exists(c.getId()));
    }
    
    @Test
    public void testRemoveCourseWithGradesAndStudentsDoesNotWork() throws Exception{
    	Course c = calculusWS2016;
    	Grade grade = new Grade(c, lecturer3, student, Mark.GOOD);
    	gradeRepository.save(grade);
    	assertTrue(courseRepository.exists(c.getId()));
    	assertTrue(!gradeRepository.findByCourseId(c.getId()).isEmpty());
    	mockMvc.perform(
    			post("/lecturer/editCourse/remove?courseId="+c.getId())
                        .with(user("lecturer3").roles(Role.LECTURER.name()))
                        .with(csrf())
    	).andExpect(
                redirectedUrl("/lecturer/courses")
        );
    	assertTrue(courseRepository.exists(c.getId()));
    }
}
