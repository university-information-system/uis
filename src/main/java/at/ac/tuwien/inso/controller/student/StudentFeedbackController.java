package at.ac.tuwien.inso.controller.student;

import static at.ac.tuwien.inso.entity.Feedback.Type.DISLIKE;
import static at.ac.tuwien.inso.entity.Feedback.Type.LIKE;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import at.ac.tuwien.inso.controller.student.forms.FeedbackForm;
import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Feedback;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.service.CourseService;
import at.ac.tuwien.inso.service.FeedbackService;
import at.ac.tuwien.inso.service.StudentService;

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
