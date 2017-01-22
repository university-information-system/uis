package at.ac.tuwien.inso.controller.student;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.service.CourseService;
import at.ac.tuwien.inso.service.StudentService;
import at.ac.tuwien.inso.service.impl.Messages;

@Controller
@RequestMapping("/student")
public class StudentCourseRegistrationController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private Messages messages;

    @PostMapping("/register")
    public String registerStudent(@RequestParam Long courseId,
                                   RedirectAttributes redirectAttributes) {
        Course course = courseService.findOne(courseId);
        if (courseService.registerStudentForCourse(course)) {
            String successMsg = messages.msg("student.my.courses.register.success", course.getSubject().getName());
            redirectAttributes.addFlashAttribute("flashMessageNotLocalized", successMsg);
            return "redirect:/student/courses";
        } else {
            String failMsg = messages.msg("student.my.courses.register.fail", course.getSubject().getName());
            redirectAttributes.addFlashAttribute("flashMessageNotLocalized", failMsg);
            return "redirect:/student/courses";
        }
    }

    @PostMapping("/unregister")
    public String unregisterStudent(@RequestParam Long course,
                                    RedirectAttributes redirectAttributes,
                                    Principal principal) {
        Student student = studentService.findByUsername(principal.getName());
        Course courseToUnregister = courseService.unregisterStudentFromCourse(student, course);

        String successMsg = messages.msg("student.my.courses.unregister.success", courseToUnregister.getSubject().getName());
        redirectAttributes.addFlashAttribute("flashMessageNotLocalized", successMsg);
        return "redirect:/student/myCourses";
    }
}
