package at.ac.tuwien.inso.controller.student;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller()
@RequestMapping("/student")
public class StudentController {

    @GetMapping
    public String index() {
        return "/student/index";
    }
}
