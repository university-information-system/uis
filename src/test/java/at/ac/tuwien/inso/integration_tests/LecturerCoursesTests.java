package at.ac.tuwien.inso.integration_tests;

import org.junit.Test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

public class LecturerCoursesTests extends AbstractCoursesTests {

    @Test
    public void itListsAllCoursesForCurrentSemesterAndLecturer() throws Exception {

        mockMvc.perform(
                get("/lecturer/courses").with(user(user1))
        ).andExpect(
                model().attribute("allCourses", expectedCoursesForLecturer1)
        );
    }

    @Test
    public void allTagsJsonTest() {
        //TODO
    }

    @Test
    public void tagsForCourseJsonTest() {
        //TODO
    }
}
