package at.ac.tuwien.inso.controller.admin;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/studyplans")
public class AdminStudyPlansController {

    @GetMapping
    public String studyplans() {
        return "admin/studyplans";
    }
}
