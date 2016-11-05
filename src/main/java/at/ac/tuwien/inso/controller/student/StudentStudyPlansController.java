package at.ac.tuwien.inso.controller.student;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student/studyplans")
public class StudentStudyPlansController {

    @GetMapping
    public String studyplans() {
        return "/student/studyplans";
    }
}
