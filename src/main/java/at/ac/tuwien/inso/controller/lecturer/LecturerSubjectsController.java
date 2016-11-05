package at.ac.tuwien.inso.controller.lecturer;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/lecturer/subjects")
public class LecturerSubjectsController {

    @GetMapping
    public String subjects() {
        return "lecturer/subjects";
    }
}
