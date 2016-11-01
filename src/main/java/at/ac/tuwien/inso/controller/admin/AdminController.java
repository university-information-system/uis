package at.ac.tuwien.inso.controller.admin;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller()
@RequestMapping("/admin")
public class AdminController {

    @GetMapping
    public String index() {
        return "/admin/index";
    }
}
