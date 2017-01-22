package at.ac.tuwien.inso.integration_tests;

import at.ac.tuwien.inso.entity.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.data.domain.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.web.servlet.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

import static java.util.Collections.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class StudentCoursesTests extends AbstractCoursesTests {

    @Test
    public void itListsAllCoursesForCurrentSemester() throws Exception {
        mockMvc.perform(
                get("/student/courses").with(user("student").roles("STUDENT"))
        ).andExpect(
                responseHasCourses(expectedCourses)
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
    
    

}
