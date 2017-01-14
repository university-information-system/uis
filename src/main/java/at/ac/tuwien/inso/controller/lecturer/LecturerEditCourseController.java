package at.ac.tuwien.inso.controller.lecturer;

import at.ac.tuwien.inso.controller.lecturer.forms.*;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.service.*;
import at.ac.tuwien.inso.service.impl.Messages;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.*;

@Controller
@RequestMapping("/lecturer/editCourse")
public class LecturerEditCourseController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private TagService tagService;

    @Autowired
    private Messages messages;

    @ModelAttribute("subject")
    private Subject getSubject(@RequestParam("courseId") Long courseId) {
        return courseService.findOne(courseId).getSubject();
    }

    @ModelAttribute("addCourseForm")
    private AddCourseForm getAddCourseForm(@RequestParam("courseId") Long courseId) {
        Course course = courseService.findOne(courseId);
        AddCourseForm form = new AddCourseForm(course);
        form.setInitialTags(tagService.findAll());
        form.setInitialActiveTags(course.getTags());
        return form;
    }


    @GetMapping
    private String getEditCoursePage(@RequestParam("courseId") Long courseId, Model model) {
        Course course = courseService.findOne(courseId);
        model.addAttribute("course", course);
        return "lecturer/editCourse";
    }

    @PostMapping
    private String updateCourse(@ModelAttribute AddCourseForm form,
                                RedirectAttributes redirectAttributes) {
        Course course = courseService.saveCourse(form);

        redirectAttributes.addFlashAttribute("flashMessageNotLocalized", messages.msg("lecturer.course.edit.success", course.getSubject().getName()));
        return "redirect:/lecturer/courses";
    }

}
