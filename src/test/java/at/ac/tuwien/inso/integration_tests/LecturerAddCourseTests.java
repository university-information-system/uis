package at.ac.tuwien.inso.integration_tests;

import at.ac.tuwien.inso.controller.lecturer.forms.AddCourseForm;
import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Role;
import org.junit.Test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

public class LecturerAddCourseTests extends AbstractCoursesTests {

    @Test
    public void itCreatesCourseTest() throws Exception {
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
    public void itDoesNotCreateCourseTest() {
        //TODO
    }
}
