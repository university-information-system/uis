package at.ac.tuwien.inso.controller.lecturer;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/lecturer/courses")
public class LecturerCoursesController {

    @GetMapping
    public String courses() {
        return "lecturer/courses";
    }
}
