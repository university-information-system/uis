package at.ac.tuwien.inso.controller.lecturer;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller()
@RequestMapping("/lecturer")
public class LecturerController {

    @GetMapping
    public String index() {
        return "/lecturer/index";
    }
}
