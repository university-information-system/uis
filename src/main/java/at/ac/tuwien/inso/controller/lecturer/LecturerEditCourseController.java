package at.ac.tuwien.inso.controller.lecturer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import at.ac.tuwien.inso.controller.lecturer.forms.AddCourseForm;
import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.entity.Tag;
import at.ac.tuwien.inso.service.CourseService;
import at.ac.tuwien.inso.service.SemesterService;
import at.ac.tuwien.inso.service.SubjectService;
import at.ac.tuwien.inso.service.TagService;

@Controller
@RequestMapping("/lecturer/editCourse")
public class LecturerEditCourseController {

    @Autowired
    private SubjectService subjectService;
    @Autowired
    private SemesterService semesterService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private TagService tagService;

    @ModelAttribute("subject")
    private Subject getSubject(@RequestParam("courseId") Long courseId) {
        return courseService.findCourseWithId(courseId).getSubject();
    }

    @ModelAttribute("addCourseForm")
    private AddCourseForm getAddCourseForm(@RequestParam("courseId") Long courseId) {
        Course course = courseService.findCourseWithId(courseId);
        AddCourseForm form = new AddCourseForm(course);
        form.setInitialTags(tagService.getAllTags());
        form.setInitialActiveTags(course.getTags());
        return form;
    }


    @GetMapping
    private String getAddCoursesPage(@RequestParam("courseId") Long courseId) {
        return "lecturer/editCourse";
    }

    @PostMapping
    private String createCourse(@ModelAttribute AddCourseForm form,
                                RedirectAttributes redirectAttributes) {
        Course course = courseService.saveCourse(form);

        redirectAttributes.addFlashAttribute("editedCourse", course);
        return "redirect:/lecturer/courses";
    }

}
