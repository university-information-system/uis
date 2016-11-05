package at.ac.tuwien.inso.controller.admin;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
public class UsersController {

    @GetMapping
    public String users() {
        return "admin/users";
    }
}
