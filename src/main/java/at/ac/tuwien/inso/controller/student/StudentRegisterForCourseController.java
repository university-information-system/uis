package at.ac.tuwien.inso.controller.student;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.*;

@Controller
@RequestMapping("/student/register")
public class StudentRegisterForCourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    private String registerStudent(@RequestParam Long courseId,
                                   RedirectAttributes redirectAttributes) {
        Course course = courseService.findOne(courseId);
        if (courseService.registerStudentForCourse(course)) {
            redirectAttributes.addFlashAttribute("registeredForCourse", course.getSubject().getName());
            return "redirect:/student/courses";
        } else {
            redirectAttributes.addFlashAttribute("notRegisteredForCourse", course.getSubject().getName());
            return "redirect:/student/courses";
        }

    }

}
