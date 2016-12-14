package at.ac.tuwien.inso.controller.student;

import at.ac.tuwien.inso.controller.student.forms.*;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.*;

import javax.validation.*;
import java.security.*;

import static at.ac.tuwien.inso.entity.Feedback.Type.*;

@Controller
@RequestMapping("/student/feedback")
public class StudentFeedbackController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private FeedbackService feedbackService;

    @PostMapping
    public String feedback(@Valid FeedbackForm form,
                           RedirectAttributes redirectAttributes,
                           Principal principal) {
        Student student = studentService.findByUsername(principal.getName());
        Course course = courseService.findOne(form.getCourse());

        Feedback feedback = new Feedback(student, course, form.isLike() ? LIKE : DISLIKE, form.getSuggestions());

        feedbackService.save(feedback);

        redirectAttributes.addFlashAttribute("flashMessage", "student.my.courses.feedback.success");
        return "redirect:/student/myCourses";
    }
}
