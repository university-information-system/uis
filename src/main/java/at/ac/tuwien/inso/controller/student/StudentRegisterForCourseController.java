package at.ac.tuwien.inso.controller.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.service.CourseService;
import at.ac.tuwien.inso.service.StudentService;

@Controller
@RequestMapping("/student/register")
public class StudentRegisterForCourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    private String registerStudent(@RequestParam Long courseId,
                                   RedirectAttributes redirectAttributes) {
        Course course = courseService.findCourseWithId(courseId);
        if (courseService.registerStudentForCourse(course)) {
            redirectAttributes.addFlashAttribute("registeredForCourse", course.getSubject().getName());
            return "redirect:/student/courses";
        } else {
            redirectAttributes.addFlashAttribute("notRegisteredForCourse", course.getSubject().getName());
            return "redirect:/student/courses";
        }

    }

}
