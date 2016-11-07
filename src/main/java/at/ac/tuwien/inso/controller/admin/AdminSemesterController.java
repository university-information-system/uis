package at.ac.tuwien.inso.controller.admin;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/semester")
public class AdminSemesterController {

    @GetMapping
    public String semester() {
        return "admin/semester";
    }
}
