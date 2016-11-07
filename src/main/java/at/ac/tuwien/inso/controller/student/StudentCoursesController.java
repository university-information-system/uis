package at.ac.tuwien.inso.controller.student;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student/courses")
public class StudentCoursesController {

    @GetMapping
    public String courses() {
        return "/student/courses";
    }
}
