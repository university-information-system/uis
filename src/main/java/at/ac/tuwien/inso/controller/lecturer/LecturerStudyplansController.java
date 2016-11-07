package at.ac.tuwien.inso.controller.lecturer;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/lecturer/studyplans")
public class LecturerStudyplansController {

    @GetMapping
    public String studyplans() {
        return "lecturer/studyplans";
    }
}
