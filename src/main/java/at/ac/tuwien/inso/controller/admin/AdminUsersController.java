package at.ac.tuwien.inso.controller.admin;

import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
public class AdminUsersController {

    @Autowired
    private UserAccountService userAccountService;

    @GetMapping
    public String users(Model model) {
        model.addAttribute("usersAttr", userAccountService.findAll());
        return "admin/users";
    }
}
