package at.ac.tuwien.inso.integration_tests;

import static junit.framework.TestCase.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.service.CourseService;
import org.junit.Test;

import at.ac.tuwien.inso.controller.lecturer.forms.AddCourseForm;
import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class LecturerAddCourseTests extends AbstractCoursesTests {

    @Autowired
    private CourseService courseService;

    @Test
    public void itCreatesCourseTest() throws Exception {
        Course course = new Course(sepm, ws2016);
        AddCourseForm form = new AddCourseForm(course);
        form.setInitialTags(tagRepository.findAll());
        form.getActiveAndInactiveTags().get(0).setActive(true);
        mockMvc.perform(
                post("/lecturer/addCourse").with(user(user1))
                        .content(form.toString())
                        .param("subjectId", form.getCourse().getSubject().getId().toString())
                        .with(csrf())
        ).andExpect(
                redirectedUrl("/lecturer/courses")
        ).andExpect(
                flash().attributeExists("flashMessageNotLocalized")
        );

        assertTrue(findCourses(lecturer1).contains(aseWS2016));
    }

    @Test
    public void itDoesNotCreateCourseTest() {
        //TODO
    }

    public List<Course> findCourses(Lecturer lecturer) {
        Iterable<Subject> subjectsForLecturer = subjectRepository.findByLecturers_Id(lecturer.getId());
        List<Course> courses = new ArrayList<>();
        subjectsForLecturer.forEach(subject -> courses.addAll(courseRepository.findAllBySemesterAndSubject(ws2016, subject)));
        return courses;
    }
}
