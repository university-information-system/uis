package at.ac.tuwien.inso.integration_tests;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.Assert.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.inso.entity.Course;

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
