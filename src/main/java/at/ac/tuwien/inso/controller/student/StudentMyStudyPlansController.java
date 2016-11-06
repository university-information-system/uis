package at.ac.tuwien.inso.controller.student;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student/my-studyplans")
public class StudentMyStudyPlansController {

    @GetMapping
    public String studyplans() {
        return "/student/my-studyplans";
    }
}
