package at.ac.tuwien.inso.controller.lecturer;

import at.ac.tuwien.inso.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/lecturer/course-details")
public class LecturerCourseDetailsController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    private String getCourseDetails(@RequestParam("courseId") Long courseId, Model model) {
        model.addAttribute("course", courseService.findOne(courseId));
        return "lecturer/course-details";
    }
}
