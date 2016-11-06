package at.ac.tuwien.inso.controller.student;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student/all-studyplans")
public class StudentAllStudyPlansController {

    @GetMapping
    public String studyplans() {
        return "/student/all-studyplans";
    }
}
