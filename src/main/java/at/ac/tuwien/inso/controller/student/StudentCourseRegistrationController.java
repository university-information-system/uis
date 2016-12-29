package at.ac.tuwien.inso.controller.student;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.*;

import java.security.*;

@Controller
@RequestMapping("/student")
public class StudentCourseRegistrationController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @GetMapping("/register")
    public String registerStudent(@RequestParam Long courseId,
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

    @PostMapping("/unregister")
    public String unregisterStudent(@RequestParam Long course,
                                    RedirectAttributes redirectAttributes,
                                    Principal principal) {
        Student student = studentService.findByUsername(principal.getName());
        courseService.unregisterStudentFromCourse(student, course);

        redirectAttributes.addFlashAttribute("flashMessage", "student.my.courses.unregister.success");
        return "redirect:/student/myCourses";
    }
}
