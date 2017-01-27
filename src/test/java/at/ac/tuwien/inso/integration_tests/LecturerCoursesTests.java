package at.ac.tuwien.inso.integration_tests;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

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
    public void allTagsJsonTest() throws Exception {

        // given 5 tags

        MvcResult result =  mockMvc.perform(
                get("/lecturer/courses/json/tags")
                        .with(user(user1))
        ).andExpect((status().isOk())
        ).andReturn();

        // the response should contain all these tags
        assertTrue(result.getResponse().getContentAsString().contains(tag1.getName()));
        assertTrue(result.getResponse().getContentAsString().contains(tag2.getName()));
        assertTrue(result.getResponse().getContentAsString().contains(tag3.getName()));
        assertTrue(result.getResponse().getContentAsString().contains(tag4.getName()));
        assertTrue(result.getResponse().getContentAsString().contains(tag5.getName()));
    }

    @Test
    public void tagsForCourseJsonTest() throws Exception {

        // given 5 tags
        // given course ase with 2 of these tags
        aseWS2016.addTags(tag3, tag5);

        MvcResult result =  mockMvc.perform(
                get("/lecturer/courses/json/tags")
                        .with(user(user1))
                        .param("courseId", aseWS2016.getId().toString())
        ).andExpect((status().isOk())
        ).andReturn();

        // the response should contain only these 2 tags
        assertFalse(result.getResponse().getContentAsString().contains(tag1.getName()));
        assertFalse(result.getResponse().getContentAsString().contains(tag2.getName()));
        assertTrue(result.getResponse().getContentAsString().contains(tag3.getName()));
        assertFalse(result.getResponse().getContentAsString().contains(tag4.getName()));
        assertTrue(result.getResponse().getContentAsString().contains(tag5.getName()));
    }
}
