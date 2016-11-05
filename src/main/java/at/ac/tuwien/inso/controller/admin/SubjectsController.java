package at.ac.tuwien.inso.controller.admin;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/subjects")
public class SubjectsController {

    @GetMapping
    public String subjects() {
        return "admin/subjects";
    }
}
